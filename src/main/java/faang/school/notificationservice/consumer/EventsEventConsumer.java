package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.EventMessageConsumer;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsEventConsumer {

    private final NotificationService notificationService;
    private final EventMessageConsumer eventMessageConsumer;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics =  "${spring.kafka.topic.events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory")
    public void handleEventListener(Map<String, Object> message) {

        EventStartEventDto eventStartEventDto = objectMapper.convertValue(message, EventStartEventDto.class);
        List<UserDto> attendeesIds = userServiceClient.getUser(eventStartEventDto.attendeesIds());
        log.info("{}", attendeesIds);
        UserDto owerUser = userServiceClient.getById(eventStartEventDto.userId());
        String text = eventMessageConsumer.buildMessage(owerUser, Locale.getDefault());
        String result = String.format("%s%s!!! %s", text, eventStartEventDto.title(), eventStartEventDto.baseMessage());
        notificationService.send(owerUser, result);
        log.info("📩 Received EventStartEvent: {}  {}", owerUser, eventStartEventDto.baseMessage());
    }
}
