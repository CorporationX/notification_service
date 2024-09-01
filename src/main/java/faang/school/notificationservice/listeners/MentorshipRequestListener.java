package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.MentorshipOfferedEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;

import java.util.List;

public class MentorshipRequestListener extends AbstractEventListener<MentorshipOfferedEvent> {

    public MentorshipRequestListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<MentorshipOfferedEvent> getEventClassType() {
        return MentorshipOfferedEvent.class;
    }
}
