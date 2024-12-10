package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RetryProperties;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.RecommendationReceivedEvent;
import faang.school.notificationservice.exception.UserContactsRetrievalException;
import faang.school.notificationservice.messaging.RecommendationMessageBuilder;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationReceivedEventListener implements MessageListener {
    private final RetryProperties retryProperties;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final RecommendationMessageBuilder recommendationMessageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent event = objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("Error while serializing {} from redis. Error: {}", messageBody, e.getMessage(), e);
        }
    }

    private void handleEvent(RecommendationReceivedEvent event) {
        try {
            UserContactsDto receiverDto = getUserContacts(event.getReceiverId());

            String message = recommendationMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale());

            notificationServices.stream()
                    .filter(service -> receiverDto.getPreference() == service.getPreferredContact())
                    .findFirst()
                    .ifPresent(service -> service.send(receiverDto, message));
            log.info("Message sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());

        } catch (UserContactsRetrievalException e) {
            log.error("Error occurred while fetching user contacts for user {}", event.getReceiverId(), e);
        } catch (RuntimeException e) {
            log.error("Error occurred while sending notification to user {}", event.getReceiverId(), e);
        }
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
            throw new UserContactsRetrievalException("Error occurred while fetching user contacts for user: " + userId + " Error: " + e.getMessage());
        }
    }
}
