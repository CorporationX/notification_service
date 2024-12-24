package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RetryProperties;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MentorshipAcceptedMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipAcceptedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RetryProperties retryProperties;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MentorshipAcceptedMessageBuilder mentorshipAcceptedMessageBuilder;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            MentorshipAcceptedEvent event = objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            logDeserializationError(message, e);
        }
    }

    private void handleEvent(MentorshipAcceptedEvent event) {
        UserContactsDto receiverDto = getUserContacts(event.getReceiverId());
        String message = mentorshipAcceptedMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale());
        notificationServices.stream()
                .filter(service -> receiverDto.getPreference() == service.getPreferredContact())
                .findFirst()
                .ifPresent(service -> service.send(receiverDto, message));
        log.info("Message sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());
    }

    @Retryable(retryFor = Exception.class,
            maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "#{@retryProperties.initialDelay}",
                    multiplierExpression = "#{@retryProperties.multiplier}",
                    maxDelayExpression = "#{@retryProperties.maxDelay}"
            )
    )
    public UserContactsDto getUserContacts(Long userId) {
        try {
            return userServiceClient.getUserContacts(userId);
        } catch (FeignException e) {
            log.error("Error occurred while fetching user contacts for user {}", userId, e);
            throw e;
        }
    }

    private void logDeserializationError(Message message, IOException e) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("Error while serializing {} from redis. Error: {}", messageBody, e.getMessage(), e);
    }
}
