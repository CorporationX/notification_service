package faang.school.notificationservice.message.producer;

public interface MessagePublisher {
    void publish(String channel, Object message);
}
