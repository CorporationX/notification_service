package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipEventListener extends AbstractEventListener<MentorshipEventDto> implements MessageListener {


    public MentorshipEventListener(UserServiceClient userService,
                                   JsonObjectMapper jsonObjectMapper,
                                   List<NotificationService> services,
                                   List<MessageBuilder<MentorshipEventDto>> messageBuilders) {
        super(userService, jsonObjectMapper, services, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipEventDto.class, event -> {
            sendMessage(event, event.getReceiverId(), event.getRequesterId());
        });
    }
}
