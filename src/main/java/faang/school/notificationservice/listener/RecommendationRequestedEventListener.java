package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisProperties;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationReceivedEvent> {

    public RecommendationRequestedEventListener(List<NotificationService> notificationServices,
                                                List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders,
                                                UserServiceClient userServiceClient,
                                                RedisProperties redisProperties, ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper, redisProperties);
    }

    @Override
    protected List<String> getTopicNameKeys() {
        return List.of("recommendation_event");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent eventDto = objectMapper.readValue(message.getBody(),
                    RecommendationReceivedEvent.class);
            log.info("New event received {}", eventDto);
            UserDto receiver = findUserById(eventDto.receiverId());
            UserDto author = findUserById(eventDto.authorId());

            String genericMessage = getMessage(Locale.getDefault(), eventDto);
            String personalMessage = genericMessage.formatted(receiver.getUsername(), author.getUsername(), eventDto.content());
            sendNotification(receiver, personalMessage);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private UserDto findUserById(long userId) {
        return userServiceClient.getUser(userId);
    }
}
