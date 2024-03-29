package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipOfferedListener extends AbstractEventListener<MentorshipOfferedEvent> {

    @Autowired
    public MentorshipOfferedListener(ObjectMapper objectMapper,
                                     List<NotificationService> services,
                                     MessageBuilder<MentorshipOfferedEvent> messageBuilder,
                                     UserServiceClient userServiceClient) {
        super(objectMapper, services, messageBuilder, userServiceClient);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        notify(message, MentorshipOfferedEvent.class);
    }
}
