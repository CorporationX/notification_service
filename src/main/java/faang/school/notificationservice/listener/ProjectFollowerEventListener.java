package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class ProjectFollowerEventListener extends AbstractEventListener<ProjectFollowerEvent> implements MessageListener {

    public ProjectFollowerEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        List<MessageBuilder<ProjectFollowerEvent>> messageBuilders,
                                        List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectFollowerEvent event = objectMapper.readValue(message.getBody(), ProjectFollowerEvent.class);
            String text = buildMessage(event, Locale.UK);
            sendNotification(event.getFollowerId(), text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
