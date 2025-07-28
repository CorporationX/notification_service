package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.messaging.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent>
        implements MessageListener {

    public SkillAcquiredEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<MessageBuilder<SkillAcquiredEvent>> messageBuilders,
                                      List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, SkillAcquiredEvent.class, event -> {
            String text = getMessage(event, Locale.getDefault());
            sendNotification(event.getUserId(), text);
            log.info("Sent notification to user {} about skill {}",
                    event.getUserId(), event.getSkillId());
        });
    }
}
