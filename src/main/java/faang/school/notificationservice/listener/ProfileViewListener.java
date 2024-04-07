package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {
    public ProfileViewListener(ObjectMapper objectMapper,
                               List<NotificationService> services,
                               MessageBuilder<ProfileViewEvent> messageBuilder,
                               UserServiceClient userServiceClient) {
        super(objectMapper, services, messageBuilder, userServiceClient);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        notify(message, ProfileViewEvent.class);
    }
}