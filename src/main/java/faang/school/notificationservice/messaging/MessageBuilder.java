package faang.school.notificationservice.messaging;

public interface MessageBuilder<T, V> {
    String buildMessage(T context, V value);
}
