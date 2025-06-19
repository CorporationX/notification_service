package faang.school.notificationservice.event;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;
import java.util.function.Consumer;

public interface EventListener<T extends Event> {
    void handleEvent(T event, Consumer<T> consumer);

    String getMessage(T event, Locale locale);

    void sendNotification(UserDto user, String message);
}
