package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.redis.RedisProperties;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("goal-complete");
    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final NotificationService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            UserDto dto = new UserDto();
            dto.setId(1L);
            dto.setEmail("email");
            dto.setPreference(UserDto.PreferredContact.EMAIL);
            service.send(dto, "test message from listener");
            log.info("Goal {} completion was saved, goalId: {}", event.goalTitle(), event.goalId());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChanelTopics(topicNameKeys, properties);
    }
}
