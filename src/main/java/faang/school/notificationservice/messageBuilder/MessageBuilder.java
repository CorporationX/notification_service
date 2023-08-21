package faang.school.notificationservice.messageBuilder;

public interface MessageBuilder<T, V> {
    String buildMessage(T event, V locale);
}
