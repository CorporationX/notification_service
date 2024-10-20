package faang.school.notificationservice.message;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale);
}
