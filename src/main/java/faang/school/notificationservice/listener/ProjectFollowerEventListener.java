package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class ProjectFollowerEventListener extends AbstractEventListener<ProjectFollowerEvent> implements MessageListener {
    public ProjectFollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                        List<NotificationService> notificationServiceList, List<MessageBuilder<ProjectFollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServiceList, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectFollowerEvent projectFollowerEvent = objectMapper.readValue(message.getBody(), ProjectFollowerEvent.class);
            String text = getMessage(projectFollowerEvent, Locale.ENGLISH, new Object[]{userServiceClient.getUser(projectFollowerEvent.getFollowerId()).getUsername()});
            sendNotification(projectFollowerEvent.getAuthorId(), text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
