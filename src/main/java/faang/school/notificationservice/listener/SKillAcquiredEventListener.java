package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SKillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {

    public SKillAcquiredEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      MessageBuilder<SkillAcquiredEvent> messageBuilder,
                                      List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillAcquiredEvent event = handleEvent(message, SkillAcquiredEvent.class);
        String userMessage = getMessage(event, Locale.getDefault());
        sendNotification(event.userId(), userMessage);
    }
}
