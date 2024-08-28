package faang.school.notificationservice.handler;

import org.springframework.scheduling.annotation.Async;

public interface EventHandler<T> {
    @Async
    public void handle(T event);
}
