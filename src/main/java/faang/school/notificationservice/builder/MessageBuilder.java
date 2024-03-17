package faang.school.notificationservice.builder;

public interface MessageBuilder<T> {
    public String buildMessage(T event);
}
