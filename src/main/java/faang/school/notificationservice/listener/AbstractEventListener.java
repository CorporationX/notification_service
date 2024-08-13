package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<?>> messageBuilders;


    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
