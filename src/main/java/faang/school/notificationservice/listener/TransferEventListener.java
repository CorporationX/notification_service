package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.TransferEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class TransferEventListener extends AbstractEventListener<TransferEvent> implements MessageListener {

    public TransferEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 Map<Class<?>, MessageBuilder<?>> messageBuilderMap) {
        super(objectMapper, userServiceClient, messageBuilderMap, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, TransferEvent.class, event -> {
            String eventMessage = getMessage(event, Locale.ENGLISH);
            sendNotification(event.getSenderId(), eventMessage);
        });
    }

    protected void handleEvent(Message redisMessage, Class<TransferEvent> clazz, Consumer<TransferEvent> consumer) {
        try {
            TransferEvent event = objectMapper.readValue(redisMessage.getBody(), clazz);
            log.info("Received TransferEvent: {}", event);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Failed to deserialize TransferEvent from Redis message", e);
        }
    }
}