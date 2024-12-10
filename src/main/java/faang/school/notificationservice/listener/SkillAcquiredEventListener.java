package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {
    @Autowired
    public SkillAcquiredEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      List<NotificationService> notificationService, MessageBuilder<SkillAcquiredEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillAcquiredEvent skillAcquiredEvent = mapMessage(message, SkillAcquiredEvent.class);
        String text = getMessage(skillAcquiredEvent, skillAcquiredEvent.getUserId());
        sendMessage(skillAcquiredEvent.getUserId(), text);
    }
}
