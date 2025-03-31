package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
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
public class LikeEventListener implements MessageListener {
    private final LikeMessageBuilder likeMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);

            UserContactsDto user = getUserContacts(event.getPostAuthorId());
            event.setPostAuthorName(user.getUsername());

            notificationServices.stream()
                    .filter(service -> user.getPreference().equals(service.getPreferredContact()))
                    .findFirst()
                    .ifPresentOrElse(
                            service -> service.send(user, likeMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale())),
                            () -> log.error("No notification service found for user {}", user.getId())
                    );
        } catch (IOException e) {
            log.error("Error while serializing like event from redis. Error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error while sending like event to user. Error: {}", e.getMessage(), e);
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
            throw e;
        }
    }
}