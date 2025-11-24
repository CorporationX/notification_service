package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.UserNotFoundException;
import faang.school.notificationservice.dto.events.NewFollowerEventDto;
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
public class NewFollowerListener extends AbstractEventListener<NewFollowerEventDto> {


    public NewFollowerListener(ObjectMapper mapper,
                               UserServiceClient userServiceClient,
                               List<NotificationService> notificationServices,
                               List<MessageBuilder<?>> messageBuilders) {
        super(mapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<NewFollowerEventDto> getEventType() {
        return NewFollowerEventDto.class;
    }

    @KafkaListener(
            topics = "${app.topics.follower-create-events}",
            groupId = "${spring.kafka.consumer.group-id:notification-service}",
            properties = "spring.json.value.default.type=" +
                    "faang.school.notificationservice.dto.events.NewFollowerEventDto"
    )
    public void onMessage(NewFollowerEventDto eventDto, Acknowledgment ack) {
        try {
            final UserDto receiver;
            try {
                receiver = loadUser(eventDto.receiverId());
            } catch (UserNotFoundException e) {
                log.warn("Receiver with id {} not found, skip notification", eventDto.receiverId());
                return;
            }

            Locale locale = resolveLocale(receiver.locale());

            String message = getMessage(eventDto, locale);
            sendNotification(receiver, message);

            ack.acknowledge();
            log.debug("Processed NewFollowerEvent followerId={} targetUserId={}",
                    eventDto.actorId(), eventDto.receiverId());
        } catch (Exception e) {
            log.error("Failed to process NewFollowerEvent: event={}", eventDto, e);
        }
    }
}