package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
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
public class RecommendationReceivedEventListener
        extends AbstractEventListener<RecommendationReceivedEvent>
        implements MessageListener {

    public RecommendationReceivedEventListener(ObjectMapper objectMapper,
                                               UserServiceClient userServiceClient,
                                               List<NotificationService> notificationServices,
                                               List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders,
                                               NotificationEventLogRepository repository
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent event = objectMapper.readValue(message.getBody(),
                    RecommendationReceivedEvent.class);
            sendNotification(event.receiverId(), getMessage(event, Locale.UK));
        } catch (IOException e) {
            throw new EventReadException(ExceptionMessage.EVENT_READ_EXCEPTION, e);
        }
    }
}
