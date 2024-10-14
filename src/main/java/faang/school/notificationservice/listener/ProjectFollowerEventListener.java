package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ProjectFollowerEventListener extends AbstractEventListener<ProjectFollowerEvent> implements MessageListener {
    public ProjectFollowerEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        MessageBuilder<ProjectFollowerEvent> messageBuilder,
                                        List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectFollowerEvent projectFollowerEvent = handleEvent(message, ProjectFollowerEvent.class);
        String notificationMessage = getMessage(projectFollowerEvent, Locale.getDefault());
        sendNotification(projectFollowerEvent.ownerId(), notificationMessage);
    }
}
