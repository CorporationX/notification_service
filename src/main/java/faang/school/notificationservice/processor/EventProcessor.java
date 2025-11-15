package faang.school.notificationservice.processor;

public interface EventProcessor<T> {
    void process(T event);
    Class<T> getEventType();
}