package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final CommentMessageBuilder commentMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            CommentEvent event = objectMapper.readValue(json, CommentEvent.class);

            UserContactsDto user = getUserContacts(event.getPostAuthorId());

            notificationServices.stream()
                    .filter(service -> user.getPreference().equals(service.getPreferredContact()))
                    .findFirst()
                    .ifPresent(service -> service.send(user, commentMessageBuilder.buildMessage(event, Locale.US)));
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

