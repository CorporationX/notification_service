package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceiveEvent;
import faang.school.notificationservice.messaging.builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationReceiveListener extends AbstractEventListener<RecommendationReceiveEvent> {

    public RecommendationReceiveListener(ObjectMapper objectMapper,
                                         UserServiceClient userServiceClient,
                                         List<NotificationService> notificationServices,
                                         List<MessageBuilder<RecommendationReceiveEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, RecommendationReceiveEvent.class);
    }

    @Override
    public void processEvent(RecommendationReceiveEvent event) {

        sendNotification(event.receiverId(), getMessage(event, Locale.ENGLISH));
    }
}