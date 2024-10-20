package faang.school.notificationservice.listener;

public interface EventListener<T> {
    void onMessage(T message);
}
