package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestedEventDto> implements MessageListener {
    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userServiceClient,
                                                List<NotificationService> notificationServices,
                                                MessageBuilder<RecommendationRequestedEventDto> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationRequestedEventDto recommendationRequestedEventDto = mapMessageBodyToEvent(message.getBody(), RecommendationRequestedEventDto.class);
        String text = getMessageBuilder().buildMessage(recommendationRequestedEventDto, Locale.getDefault());
        sendNotification(recommendationRequestedEventDto.getReceiverId(), text);
    }
}
