package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
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
                                        List<NotificationService> notificationServices,
                                        List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectFollowerEvent.class, event -> {
            UserDto requesterDto = userServiceClient.getUser(event.getCreatorId());
            String notificationMessage = getMessage(event, Locale.UK);
            sendNotification(requesterDto, notificationMessage);
        });
    }
}