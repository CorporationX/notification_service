package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FollowerEventListener extends AbstractEventListener implements MessageListener {

    private final JsonObjectMapper jsonObjectMapper;

    public FollowerEventListener(List<NotificationService> services, List<MessageBuilder> messageBuilders,
                                 UserServiceClient userService, JsonObjectMapper jsonObjectMapper) {
        super(services, messageBuilders, userService);
        this.jsonObjectMapper = jsonObjectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("FollowerEventListener has received a new message");
        FollowerEventDto followerEventDto = jsonObjectMapper.readValue(message.getBody(), FollowerEventDto.class);
        sendMessage(followerEventDto);
    }
}
