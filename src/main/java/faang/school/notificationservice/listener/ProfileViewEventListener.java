package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<ProfileViewEvent> messageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("Error while deserializing {} from Redis. Error: {}", messageBody, e.getMessage(), e);
        }
    }

    private void handleEvent(ProfileViewEvent event) {
        UserContactsDto receiverDto = getUserContacts(event.getProfileId());

        String notificationMessage = messageBuilder.buildMessage(event, LocaleContextHolder.getLocale());

        notificationServices.stream()
                .filter(service -> receiverDto.getPreference() == service.getPreferredContact())
                .findFirst()
                .ifPresent(service -> service.send(receiverDto, notificationMessage));

        log.info("Notification sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());
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
}
