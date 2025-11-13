package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.TimeLeft;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserIdsClientDto;
import faang.school.notificationservice.messaging.EventMessageConsumer;
import faang.school.notificationservice.service.EventNotificationService;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsEventConsumer {

    private final EventNotificationService eventNotificationService;
    private final EventMessageConsumer eventOwnerMessageConsumer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory")
    public void handleEventListener(@Payload Map<String, Object> message, Acknowledgment  ack) {

        EventStartEventDto eventStartEventDto = objectMapper.convertValue(message, EventStartEventDto.class);

        String text = eventOwnerMessageConsumer.buildMessage(eventStartEventDto, Locale.getDefault());

        log.info("Information about the event has arrived! event id - {}, owner id-{} and name-{}, name event -{}",
                eventStartEventDto.eventId(), eventStartEventDto.userId(),
                eventStartEventDto.nameOwner(), eventStartEventDto.titleEvent());

        List<UserDto> attendeesIds = eventStartEventDto.attendeesUser();

        if (attendeesIds.isEmpty()) {
            log.info("There are no subscribers to the event {}.", eventStartEventDto.eventId());
        } else {
            attendeesIds.forEach(user -> eventNotificationService.send(user
                    , text));
        }
        log.info("Received EventStartEvent: {} ", eventStartEventDto.nameOwner());
        ack.acknowledge();
    }
}
