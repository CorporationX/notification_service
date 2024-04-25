package faang.school.notificationservice.service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    @Autowired
    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEvent>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ExecutorService service = Executors.newFixedThreadPool(4);
        try {
            EventStartEvent event = objectMapper.readValue(message.getBody(), EventStartEvent.class);
            System.out.println( "event.toString() = " + event.toString() );
            List<Long> attendees = event.getAttendeeIds();
            attendees.forEach(attendee -> service.submit(() -> notifyAttendees(attendee, getMessage(event, Locale.US))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}