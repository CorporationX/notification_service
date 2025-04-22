package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
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
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders,
                                 NotificationEventLogRepository repository
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            sendNotification(event.followeeId(), getMessage(event, Locale.UK));
        } catch (IOException e) {
            throw new EventReadException(ExceptionMessage.EVENT_READ_EXCEPTION, e);
        }
    }
}
