package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
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

    public AchievementEventListener(UserServiceClient userService,
                                    JsonObjectMapper jsonObjectMapper,
                                    List<NotificationService> services,
                                    List<MessageBuilder<AchievementEventDto>> messageBuilders) {
        super(userService, jsonObjectMapper, services, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("AchievementEventListener has received a new message");
        handleEvent(message, AchievementEventDto.class, event -> sendMessage(event, event.getUserId(), event.getUserId()));
    }
}
