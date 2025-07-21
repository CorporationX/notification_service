package faang.school.notificationservice.listener;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public abstract class DirectNotificationEventListener<T extends NotificationEvent> extends AbstractEventListener<T> {

    protected final NotificationSenderService notificationSender;
    private final MessageBuilder<T> messageBuilder;

    protected void sendNotification(T event) {
        if (isEventValid(event)) {
            notificationSender.send(event.getOwner(), getMessage(event));
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    protected String getMessage(T event) {
        Locale locale = Objects.isNull(event.getOwner().getLocale())
                ? LocaleContextHolder.getLocale()
                : event.getOwner().getLocale();
        return messageBuilder.buildMessage(event, locale);
    }
}