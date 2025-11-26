package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<T> {
    /**
     * Конкретный класс события, который строит этот билдер.
     */
    Class<?> getInstance();

    /**
     * Сформировать локализованное сообщение для события.
     */
    String buildMessage(T event, Locale locale);
}
