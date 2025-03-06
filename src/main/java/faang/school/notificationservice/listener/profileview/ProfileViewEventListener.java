package faang.school.notificationservice.listener.profileview;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.UserService;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {


    public ProfileViewEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<ProfileViewEvent>> messageBuilders) {
        super(objectMapper, userService, notificationServices, messageBuilders);
    }

    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent event = getEventFromBytes(message.getBody(), ProfileViewEvent.class);
        UserDto userDto = userService.getUserById(event.getReceiverId());
        String eventMessage = getMessage(event, Locale.getDefault());

        sendNotification(userDto.getId(), eventMessage);
    }
}
