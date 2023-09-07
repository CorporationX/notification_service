package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventRecommendationRequestDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationRequestListener extends AbstractEventListener<EventRecommendationRequestDto> {

    @Autowired
    public RecommendationRequestListener(ObjectMapper objectMapper,
                                         UserServiceClient userServiceClient,
                                         List<NotificationService> notificationServices,
                                         List<MessageBuilder<EventRecommendationRequestDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventRecommendationRequestDto event = deserializeJson(message, EventRecommendationRequestDto.class);
        String messageForNotification = getMessage(MessageBuilder.class, event);
        sendNotification(event.getReceiverId(), messageForNotification);
    }
}
