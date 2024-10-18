package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.SkillOfferedEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SkillOfferedEventListener extends AbstractEventListener<SkillOfferedEvent> implements MessageListener {

    public SkillOfferedEventListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     MessageBuilder<SkillOfferedEvent> messageBuilder,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferedEvent event = handleEvent(message, SkillOfferedEvent.class);
        String userMessage = getMessage(event, Locale.getDefault());
        sendNotification(event.receiverId(), userMessage);
    }
}
