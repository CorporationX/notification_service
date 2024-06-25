package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected T getEvent(Message message, Class<T> type) {
        try {
            return objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            log.error("mapping was failed", e);
            throw new RuntimeException("mapping was failed");
        }
    }

    protected String getMessage(T event, Locale userLocale, NotificationData notificationData) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale, notificationData))
                .orElseThrow(() -> new IllegalArgumentException(String.format("There are no builders for event: %s", event.getClass().getName())));
    }

    protected void sendNotification(UserNotificationDto userNotificationDto, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userNotificationDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("There is no notification service for user preference. User id: %s", userNotificationDto.getId())))
                .send(userNotificationDto, message);
    }

    protected NotificationData getNotificationData(long followerId){
        UserNotificationDto follower = userServiceClient.getDtoForNotification(followerId);
        return NotificationData.builder()
                .follower(follower.getUsername())
                .build();
    }
}