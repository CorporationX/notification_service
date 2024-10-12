package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {

    public SkillAcquiredEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, SkillAcquiredEvent.class, event -> {
            UserDto requesterDto = userServiceClient.getUser(event.getUserId());
            String notificationMessage = getMessage(event, Locale.UK);
            sendNotification(requesterDto, notificationMessage);
        });
    }
}
