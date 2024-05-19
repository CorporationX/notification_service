package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecommendationRequestEventListener extends AbstractEventListener<RecommendationRequestEvent> implements MessageListener {


    public RecommendationRequestEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                              List<NotificationService> notificationService,
                                              MessageBuilder<RecommendationRequestEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern){
        handleEvent(message, RecommendationRequestEvent.class);
    }
}
