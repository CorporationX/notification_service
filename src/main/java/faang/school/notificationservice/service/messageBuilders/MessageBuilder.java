package faang.school.notificationservice.service.messageBuilders;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage(T event, Locale userLocale);

    Class<?> supportsEventType();
}
