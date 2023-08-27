package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AchievementEventListener extends AbstractEventListener implements MessageListener {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List list, List list2) {
        super(objectMapper, userServiceClient, list, list2);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("AchievementEventListener has received a new message");
        AchievementEventDto achievementEventDto = objectMapper.readValue(message.getBody(), AchievementEventDto.class);
        sendMessage(achievementEventDto);
    }
}
