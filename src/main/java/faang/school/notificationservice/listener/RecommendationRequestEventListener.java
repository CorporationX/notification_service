package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationRequestEventListener extends AbstractListenerNotification<RecommendationRequestEvent> implements MessageListener {

    public RecommendationRequestEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                              List<NotificationService> notificationService,
                                              List<MessageBuilder<RecommendationRequestEvent>> messageBuilders){
        super(objectMapper,userServiceClient,notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern){
        try {
            RecommendationRequestEvent recommendationRequestEvent = objectMapper.readValue(message.getBody(), RecommendationRequestEvent.class);
            String text = getMessage(recommendationRequestEvent, Locale.ENGLISH);
            sendNotification(recommendationRequestEvent.getReceiverId(), text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
