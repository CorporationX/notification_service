package faang.school.notificationservice.listener.event;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.EventStartNotificationEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class EventStartEventListener extends AbstractEventListener<EventStartNotificationEvent> {

    public EventStartEventListener(
            List<NotificationService> notificationServices,
            MessageBuilder<EventStartNotificationEvent> messageBuilder) {

        super(notificationServices, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.event-start-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaEventStartEventListener"
    )
    public void listenerEventStart(EventStartNotificationEvent event) {
        sendNotification(event);
    }

    public void sendNotification(EventStartNotificationEvent event) {
        if (isEventValid(event)) {
            List<UserDto> users = splitEvent(event);
            users.forEach(userDto -> sendMessage(userDto, getMessage(event)));
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    public List<UserDto> splitEvent(EventStartNotificationEvent event) {
        List<UserDto> attendees = event.getAttendees();
        UserDto owner = event.getOwner();

        List<UserDto> users = new ArrayList<>();
        users.add(owner);
        users.addAll(attendees);

        return users;
    }

    @Override
    public boolean isEventValid(EventStartNotificationEvent event) {
        boolean eventIsValid = Objects.nonNull(event);
        boolean ownerIsValid = isUserDtoValid(event.getOwner());
        boolean attendeesIsValid = event.getAttendees().stream()
                .allMatch(this::isUserDtoValid);
        return eventIsValid && ownerIsValid && attendeesIsValid;
    }


}
