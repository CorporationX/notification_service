package faang.school.notificationservice.messagebuilder;

public interface MessageBuilder<T> {

    String buildMessage(T event);

    Class<?> getEventType();
}
