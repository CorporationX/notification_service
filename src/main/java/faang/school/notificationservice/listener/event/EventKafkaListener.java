package faang.school.notificationservice.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaEventTopicProperties;
import faang.school.notificationservice.dto.client.user_service.EventDto;
import faang.school.notificationservice.dto.client.user_service.UserDto;
import faang.school.notificationservice.listener.AbstractKafkaListener;
import faang.school.notificationservice.mapper.kafka.KafkaMapper;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.kafka.event.EventMessage;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EventKafkaListener extends AbstractKafkaListener<EventDto, EventMessage> {

    private final KafkaEventTopicProperties topicProperties;
    private final UserServiceClient userService;

    public EventKafkaListener(KafkaEventTopicProperties topicProperties,
                              UserServiceClient userService,
                              List<NotificationService> notificationServices,
                              List<MessageBuilder<EventMessage>> messageBuilders,
                              ObjectMapper objectMapper,
                              Class<EventDto> eventType,
                              Class<EventMessage> messageType) {
        super(notificationServices, messageBuilders, objectMapper,
                eventType, messageType);

        this.topicProperties = topicProperties;
        this.userService = userService;
    }

    @KafkaListener(
            topics = "${spring.data.kafka.topic.event.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenEventTopic(String message) {
        EventDto eventDto = getEvent(message);
        log.info("Received a message from {}: {}", topicProperties.getName(), eventDto);

        String initiatorName = userService.getUserById(eventDto.getOwnerId()).getUsername();
        List<UserDto> attendees = eventDto.getAttendeeIds().stream()
                .map(userService::getUserById)
                .toList();

        EventMessage eventMessage = KafkaMapper.toEventMessage(eventDto, initiatorName);
        attendees.forEach(user -> {
            String text = getMessage(eventMessage, user.getLocale());
            sendNotification(user, text);
        });
    }
}
