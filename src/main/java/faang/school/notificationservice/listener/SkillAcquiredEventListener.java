package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillAcquiredEvent;
import faang.school.notificationservice.exception.EventReadException;
import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.repository.NotificationEventLogRepository;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {
    public SkillAcquiredEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<SkillAcquiredEvent>> messageBuilders,
                                      NotificationEventLogRepository repository
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, null);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SkillAcquiredEvent event = objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class);
            sendNotification(event.getUserId(), getMessage(event, Locale.ROOT));
        } catch (IOException e) {
            throw new EventReadException(ExceptionMessage.EVENT_READ_EXCEPTION, e);
        }
    }
}
