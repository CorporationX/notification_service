package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;

import java.util.List;

public abstract class AbstractKafkaEventListener<E, T> extends AbstractEventListener<E> implements EventListener<T> {

    protected AbstractKafkaEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<E>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //do nothing
    }
}
