package faang.school.notificationservice.service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEvent>  {

    @Autowired
    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEvent>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    protected Class<EventStartEvent> getEventType() {
        return EventStartEvent.class;
    }

    @Override
    protected List<Long> getAttendeeIds(EventStartEvent event) {
        return event.getAttendeeIds();
    }
}