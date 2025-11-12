package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.TimeLeft;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.EventMessageConsumer;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class EventsEventConsumer {

    @Autowired
    @Qualifier("eventNotificationService")
    private final NotificationService notificationService;
    private final EventMessageConsumer eventOwnerMessageConsumer;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    public EventsEventConsumer(@Qualifier("eventNotificationService") NotificationService notificationService,
                               EventMessageConsumer eventOwnerMessageConsumer,
                               UserServiceClient userServiceClient,
                               ObjectMapper objectMapper) {

        this.notificationService = notificationService;
        this.eventOwnerMessageConsumer = eventOwnerMessageConsumer;
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics =  "${spring.kafka.topic.events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory")
    public void handleEventListener(Map<String, Object> message) {

        EventStartEventDto eventStartEventDto = objectMapper.convertValue(message, EventStartEventDto.class);

        UserDto owerUser = userServiceClient.getById(eventStartEventDto.userId());

        String text = eventOwnerMessageConsumer.buildMessage(eventStartEventDto, Locale.getDefault());
        notificationService.send(eventStartEventDto.userId(), text);

        eventStartEventDto.attendeesIds()
                        .forEach(id -> notificationService.send(id, text));
        log.info("📩 Received EventStartEvent: {} ", owerUser);
    }
}
