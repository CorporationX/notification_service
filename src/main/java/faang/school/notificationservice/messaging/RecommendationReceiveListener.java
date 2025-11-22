package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationEvent;
import faang.school.notificationservice.messaging.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationReceiveListener extends AbstractEventListener<RecommendationEvent> {

    public RecommendationReceiveListener(ObjectMapper objectMapper,
                                         UserServiceClient userServiceClient,
                                         List<MessageBuilder<RecommendationEvent>> messageBuilders,
                                         List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices, RecommendationEvent.class);
    }

    @Override
    public void eventConsumer(RecommendationEvent event) {

        sendNotification(event.receiverId(), getMessage(event, Locale.ENGLISH));
    }
}