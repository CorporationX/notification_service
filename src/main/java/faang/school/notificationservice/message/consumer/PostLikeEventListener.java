package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.message.event.PostLikeEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PostLikeEventListener extends AbstractEventListener<PostLikeEvent> implements MessageListener {

    protected PostLikeEventListener(ObjectMapper mapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    MessageBuilder<PostLikeEvent> messageBuilder) {
        super(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostLikeEvent.class);
    }
}
