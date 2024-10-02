package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.MentorshipAcceptedEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MentorshipAcceptedListener extends AbstractEventListener<MentorshipAcceptedEvent> {

    public MentorshipAcceptedListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<MentorshipAcceptedEvent> getEventClassType() {
        return MentorshipAcceptedEvent.class;
    }
}
