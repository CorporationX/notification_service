package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.EventMessageConsumer;
import faang.school.notificationservice.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsEventConsumer {

    private final NotificationServiceImpl notificationService;
    private final EventMessageConsumer eventMessageConsumer;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics =  "${spring.kafka.topic.events}")
    public void handleEventListener(Map<String, Object> message) {

        EventStartEventDto eventStartEventDto = objectMapper.convertValue(message, EventStartEventDto.class);
        //List<UserDto> attendeesIds = userServiceClient.getUser(eventStartEventDto.attendeesIds());
        UserDto owerUser = userServiceClient.getById(eventStartEventDto.userId());
        log.info("📩 Received EventStartEvent: {} ", owerUser);
    }
}
