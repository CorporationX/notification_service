package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {
    @Autowired
    public SkillAcquiredEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      List<NotificationService> notificationService, MessageBuilder<SkillAcquiredEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("mapping message using mapMessage");
        SkillAcquiredEvent skillAcquiredEvent = mapMessage(message, SkillAcquiredEvent.class);
        log.info("generate text using getMessage");
        String text = getMessage(skillAcquiredEvent, skillAcquiredEvent.getUserId());
        log.info("send message using sendMessage");
        sendMessage(skillAcquiredEvent.getUserId(), text);
    }
}
