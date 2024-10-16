package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener implements MessageListener {
    final ObjectMapper objectMapper;
    final UserServiceClient userServiceClient;
    final List<NotificationService> notificationServices;

    @Override
    public abstract void onMessage(Message message, byte[] pattern);
}
