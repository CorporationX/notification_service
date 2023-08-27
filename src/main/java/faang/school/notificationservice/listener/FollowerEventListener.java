package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    @Autowired
    public FollowerEventListener(List<NotificationService> services, List<MessageBuilder> messageBuilders,
                                 UserServiceClient userService, JsonObjectMapper jsonObjectMapper) {
        super(services, messageBuilders, userService,jsonObjectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, event -> sendMessage(event, event.getFolloweeId(),event.getFollowerId()));
    }
}
