package faang.school.notificationservice.message;

public interface MessageBuilder<T> {
    String buildMessage(T event, String Username);
}
