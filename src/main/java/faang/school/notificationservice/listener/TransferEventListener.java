package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.TransferSentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Component
public class TransferEventListener extends AbstractEventListener<TransferSentEvent> implements MessageListener {

    public TransferEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<TransferSentEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Consumer<TransferSentEvent> handler = (event) -> {
            String eventMessage = super.getMessage(event, Locale.ENGLISH);
            super.sendNotification(event.getSenderId(), eventMessage);
        };
        super.handleEvent(message, TransferSentEvent.class, handler);
    }
}
