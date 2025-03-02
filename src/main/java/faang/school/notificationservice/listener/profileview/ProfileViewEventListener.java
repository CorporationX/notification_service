package faang.school.notificationservice.listener.profileview;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewEventListener(ObjectMapper objectMapper, NotificationStrategyService notificationStrategyService, UserServiceClient userServiceClient, MessageBuilder<ProfileViewEvent> messageBuilder) {
        super(objectMapper, notificationStrategyService, userServiceClient, messageBuilder);
    }

    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent event = handleEvent(message.getBody(), ProfileViewEvent.class);
        UserDto userDto = userServiceClient.getUser(event.getUserId());
        String eventMessage = messageBuilder.buildMessage(event, Locale.getDefault());

        sendNotification(userDto, eventMessage);
    }
}
