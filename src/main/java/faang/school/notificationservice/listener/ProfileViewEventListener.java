package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProfileViewEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    private final UserServiceClient userServiceClient;
    public ProfileViewEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        List<NotificationService> notificationServices,
                                        List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event -> {
            UserDto profileOwnerDto = userServiceClient.getUser(event.getProfileOwnerId());
            String notificationMessage = buildMessage(event, Locale.UK);
            sendNotification(profileOwnerDto, notificationMessage);
            log.info("Notification was sent, profileOwnerId: {}, notificationMessage: {}",
                    profileOwnerDto.getId(), notificationMessage);
        });
    }
}
