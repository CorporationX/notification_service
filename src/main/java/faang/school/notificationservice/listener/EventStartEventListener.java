package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventStartEvent>implements MessageListener {

    private final UserServiceClient userServiceClient;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public EventStartEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                   List<MessageBuilder<?>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, EventStartEvent.class, event -> {
            List<Long> participantIds = event.getParticipantIds();
            String notificationMessage = buildMessage(event, Locale.UK);

            for (Long participantId : participantIds) {
                UserDto eventParticipantDto = userServiceClient.getUser(participantId);
                sendNotification(eventParticipantDto, notificationMessage);
                log.info("Notification was sent, eventParticipantDto: {}, notificationMessage: {}", eventParticipantDto.getId(), notificationMessage);
            }
        });
    }
}
