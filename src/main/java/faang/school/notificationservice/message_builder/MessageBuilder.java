package faang.school.notificationservice.message_builder;

import java.util.Locale;

public interface MessageBuilder<T> {
    String getMessage(T object, Locale locale);
}
