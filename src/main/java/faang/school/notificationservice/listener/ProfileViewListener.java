package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProfileViewListener implements MessageListener {
    private final AbstractEventListener<ProfileViewEvent> eventListener;

    public ProfileViewListener(ObjectMapper objectMapper,
                                 List<NotificationService> services,
                                 List<MessageBuilder<ProfileViewEvent>> messageBuilders,
                                 UserServiceClient userServiceClient,
                                 UserContext userContext) {

        this.eventListener = new AbstractEventListener<>(objectMapper,
                services,
                messageBuilders,
                userServiceClient,
                userContext
        );
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        eventListener.buildAndSendMessage(message, ProfileViewEvent.class);
        System.out.println("i am here");
    }
}