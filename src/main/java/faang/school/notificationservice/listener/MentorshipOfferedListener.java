package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedListener extends AbstractEventListener<MentorshipOfferedEvent> implements MessageListener {

    @Autowired
    public MentorshipOfferedListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                     List<NotificationService> notificationServices, UserContext userContext) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices, userContext);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event -> {
            UserDto receiver = userServiceClient.getUser(event.getReceiver());
            String text = getMessage(event, Locale.UK);
            sendNotification(receiver, text);
        });
    }
}
