package faang.school.notificationservice.util;

public interface MessageBuilder<T, V> {
    String buildMessage(T event, V locale);
}
