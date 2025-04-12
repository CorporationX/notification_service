package faang.school.notificationservice.messaging.listener;

import faang.school.notificationservice.messaging.event.EventStartEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStartEventListener implements MessageListener {

    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEvent event = (EventStartEvent) SerializationUtils.deserialize(message.getBody());

        String eventId = event.getEventId();
        List<UserDto> participants = event.getParticipants();

        String messageToSend = "Событие " + eventId + " начинается прямо сейчас!";

        for (UserDto participant : participants) {
            notificationService.send(participant, messageToSend);
        }
    }
}
