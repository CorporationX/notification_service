package faang.school.notificationservice.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Locale;
import java.util.function.Consumer;

public interface EventListener<T extends Event> {
    void handleEvent(ConsumerRecord<String, String> message, Class<T> type, Consumer<T> consumer);

    String getMessage(T event, Locale locale);

    void sendNotification(Long userId, String message);
}
