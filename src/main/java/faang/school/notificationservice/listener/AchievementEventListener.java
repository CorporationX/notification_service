package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.AchievementEventDto;
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
public class AchievementEventListener extends AbstractEventListener<AchievementEventDto> implements MessageListener {

    public AchievementEventListener(List<NotificationService> services, List<MessageBuilder> messageBuilders,
                                    UserServiceClient userService, JsonObjectMapper jsonObjectMapper) {
        super(services, messageBuilders, userService, jsonObjectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("AchievementEventListener has received a new message");
        handleEvent(message, AchievementEventDto.class, event -> sendMessage(event, event.getUserId()));
    }
}
