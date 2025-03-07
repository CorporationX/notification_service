package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractEventListener<T> {
    private final UserServiceClient userServiceClient;
    private final Map<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap;
    private final Map<Class<?>, MessageBuilder<?>> messageBuildersMap;

    public String getMessage(T event, Locale locale) {
        Class<?> eventClass = event.getClass();
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) messageBuildersMap.get(eventClass);
        if (messageBuilder == null) {
            throw new IllegalStateException("Message builder for event " + event + " not found");
        }
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(long userId, String message) {
        UserNotificationDto userNotificationDto = userServiceClient.getUserNotificationDto(userId);
        NotificationService notificationService = notificationServicesMap.get(userNotificationDto.getPreference());
        if (notificationService == null) {
            throw new IllegalStateException("Notification service for user " + userId + " not found");
        }
        notificationService.send(userNotificationDto, message);
    }
}
