package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.events.RequestEventDto;
import faang.school.notificationservice.error.UserNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.core.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RequestStatusListener extends AbstractEventListener<RequestEventDto> {

    public RequestStatusListener(ObjectMapper mapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<?>> messageBuilders) {
        super(mapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<RequestEventDto> getEventType() {
        return RequestEventDto.class;
    }

    @KafkaListener(
            topics = "${app.topics.request-status-events}",
            groupId = "${spring.kafka.consumer.group-id:notification-service}",
            properties = "spring.json.value.default.type=" +
                    "faang.school.notificationservice.dto.events.RequestEventDto"
    )

    public void onMessage(RequestEventDto eventDto, Acknowledgment ack) {
        try {
            log.info("Received request status: {}", eventDto);

            final UserDto receiver;
            try {
                receiver = loadUser(eventDto.userId());
            } catch (UserNotFoundException e) {
                log.warn("User with id {} not found, skip notification", eventDto.userId());
                ack.acknowledge();
                return;
            }

            Locale locale = resolveLocale(receiver.locale());
            String message = getMessage(eventDto, locale);
            sendNotification(receiver, message);

            ack.acknowledge();
            log.debug("Processed RequestEvent: requestId={}, userId={}, status={}",
                    eventDto.requestId(), eventDto.userId(), eventDto.status());
        } catch (Exception e) {
            log.error("Failed to process RequestEvent: {}", eventDto, e);
        }
    }
}