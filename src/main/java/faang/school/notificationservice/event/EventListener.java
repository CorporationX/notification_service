package faang.school.notificationservice.event;

import faang.school.notificationservice.dto.UserDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Locale;
import java.util.function.Consumer;

public interface EventListener<T extends Event> {
    void handleEvent(ConsumerRecord<String, String> message, Class<T> type, Consumer<T> consumer);

    String getMessage(T event, Locale locale);

    void sendNotification(UserDto user, String message);
}
