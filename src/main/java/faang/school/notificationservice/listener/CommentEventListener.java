package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {

    @Autowired
    public CommentEventListener(ObjectMapper objectMapper,
                                List<NotificationService> services,
                                MessageBuilder<CommentEventDto> messageBuilder,
                                UserServiceClient userServiceClient) {
        super(objectMapper, services, messageBuilder, userServiceClient);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        notify(message, CommentEventDto.class);
    }
}