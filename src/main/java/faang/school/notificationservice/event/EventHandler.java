package faang.school.notificationservice.event;

public interface EventHandler<T> {
    void handle(T event);
}
