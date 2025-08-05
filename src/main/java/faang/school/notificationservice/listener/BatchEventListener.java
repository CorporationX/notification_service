package faang.school.notificationservice.listener;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.service.notification.handler.NotificationEventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class BatchEventListener<T extends NotificationEvent> extends AbstractEventListener<T> {

    protected final NotificationEventHandler<T> eventHandler;

    protected BatchEventListener(NotificationEventHandler<T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    protected void processEventsBatch(List<T> events) {
        if (events.isEmpty()) {
            log.info("No events to process");
            return;
        }

        Map<Boolean, List<T>> partitionedEvents = events.stream()
                .collect(Collectors.partitioningBy(this::isEventValid));

        List<T> validEvents = partitionedEvents.get(true);
        List<T> invalidEvents = partitionedEvents.get(false);

        eventHandler.saveNotifications(validEvents);

        invalidEvents.forEach(event -> log.error("Event validation failed. Event: {}", event));
    }
}
