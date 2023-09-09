package faang.school.notificationservice.messageBuilder;


import java.util.Locale;

public interface MessageBuilder<T, V> {
    String buildMessage(T event, Locale locale, V argument);

    Class<?> getEventType();
}
