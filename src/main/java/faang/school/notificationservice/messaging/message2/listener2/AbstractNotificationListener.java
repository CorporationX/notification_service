package faang.school.notificationservice.messaging.message2.listener2;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractNotificationListener<T> {
    private final List<NotificationService> notifications;

    protected void handle(UserDto user, T event, MessageBuilder<T> messageBuilder, Locale locale) {
        if (user.getPreference() == null) {
            log.info("User did not specify a preferred contact method. Notification was not sent.");
            return;
        }
        notifications.stream()
                .filter(notification -> notification.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresentOrElse(
                        notification -> notification.send(user, messageBuilder.buildMessage(event, locale)),
                        () -> log.warn("No matching notification service found for user preference: {}", user.getPreference())
                );
    }
}
