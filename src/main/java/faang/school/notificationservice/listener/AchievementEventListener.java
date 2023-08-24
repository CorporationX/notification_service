package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEventDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AchievementEventListener extends AbstractEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    public AchievementEventListener(List<NotificationService> services, List<MessageBuilder> messageBuilders, UserServiceClient userService, ObjectMapper objectMapper) {
        super(services, messageBuilders, userService);
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("AchievementEventListener has received a new message");
        try {
            AchievementEventDto eventDto = objectMapper.readValue(message.getBody(), AchievementEventDto.class);
            sendMessage(eventDto);
        } catch (IOException e) {
            log.error("IOException while parsing message in AchievementEventListener...");
            throw new RuntimeException(e);
        }
    }
}
