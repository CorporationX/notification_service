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
    private final List<MessageBuilder<?>> messageBuilders;

    protected void handle(UserDto user, T event, Locale locale) {
        if (user.getPreference() == null) {
            log.info("User did not specify a preferred contact method. Notification was not sent.");
            return;
        }

        @SuppressWarnings("unchecked")
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder -> builder.getInstance().isAssignableFrom(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No MessageBuilder found for event: " + event.getClass()));


        notifications.stream()
                .filter(notification -> notification.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresentOrElse(
                        notification -> notification.send(user, messageBuilder.buildMessage(event, locale)),
                        () -> log.warn("No matching notification service found for user preference: {}", user.getPreference())
                );
    }
}
