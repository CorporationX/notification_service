package faang.school.notificationservice.builder;

public interface MessageBuilder<T> {

    String buildMessage(T event);

    Class<?> supportsEventType();
}
