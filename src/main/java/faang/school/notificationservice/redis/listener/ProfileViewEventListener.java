package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.ProfileViewEventDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {
    public ProfileViewEventListener(UserServiceClientMock userServiceClient,
                                    List<MessageBuilder<?>> messageBuilders,
                                    List<NotificationService> notificationServices,
                                    ObjectMapper objectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, objectMapper, ProfileViewEventDto.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEvent(ProfileViewEventDto event) {
        UserDto userDto = getUserDto(event.getActorId());

        MessageBuilder<ProfileViewEventDto> messageBuilder =
                (MessageBuilder<ProfileViewEventDto>) defineBuilder(ProfileViewEventDto.class);
        String message = messageBuilder.buildMessage(event, userDto.getLocale());

        sendNotification(userDto, message);
    }
}
