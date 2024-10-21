package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.MentorshipRequestAcceptedDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipRequestAcceptedListener extends AbstractEventListener<MentorshipRequestAcceptedDto> {
    public MentorshipRequestAcceptedListener(UserServiceClientMock userServiceClient,
                                             List<MessageBuilder<?>> messageBuilders,
                                             List<NotificationService> notificationServices,
                                             ObjectMapper objectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, objectMapper, MentorshipRequestAcceptedDto.class);
    }

    @Override
    protected void processEvent(MentorshipRequestAcceptedDto event) {
        MessageBuilder<MentorshipRequestAcceptedDto> messageBuilder = defineBuilder();

        UserDto userDto = getUserDto(event.getActorId());
        log.info("User with id {} was founded", userDto.getId());
        String message = messageBuilder.buildMessage(event, userDto.getLocale());
        sendNotification(userDto, message);
        log.info("Message {} has been send to user {}", message, userDto.getUsername());
    }
}
