package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.message.event.RecommendationReceivedEvent;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {

    protected RecommendationReceivedEventListener(ObjectMapper mapper,
                                                  UserServiceClient userServiceClient,
                                                  List<NotificationService> notificationServices,
                                                  MessageBuilder<RecommendationReceivedEvent> messageBuilder) {
        super(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    @Retryable(maxAttempts = 5, backoff = @Backoff(multiplier = 2.0))
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationReceivedEvent.class);
    }
}
