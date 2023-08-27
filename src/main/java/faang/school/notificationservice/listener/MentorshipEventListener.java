package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipEventListener extends AbstractEventListener<MentorshipEventDto> implements MessageListener {


    public MentorshipEventListener(List<NotificationService> services,
                                   List<MessageBuilder> messageBuilders,
                                   UserServiceClient userService,
                                   JsonObjectMapper jsonObjectMapper) {
        super(services, messageBuilders, userService, jsonObjectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipEventDto.class, event -> {
            UserDto requester = userService.getUser(event.getRequesterId());
            sendMessage(event, requester.getId());
        });

    }
}
