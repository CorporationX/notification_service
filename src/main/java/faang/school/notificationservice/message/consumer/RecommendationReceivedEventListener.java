package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.message.event.RecommendationReceivedEvent;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationReceivedEvent.class);
    }
}
