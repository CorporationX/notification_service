package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import faang.school.notificationservice.model.event.UserFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestedEvent> implements MessageListener {

    private final UserServiceClient userServiceClient;
    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userServiceClient,
                                                List<NotificationService> notificationServices,
                                                List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationRequestedEvent.class, event -> {
            UserDto receiverDto = userServiceClient.getUser(event.getReceiverId());
            String notificationMessage = buildMessage(event, Locale.UK);
            sendNotification(receiverDto, notificationMessage);
            log.info("Notification was sent, recommendationReceiverId: {}, notificationMessage: {}", receiverDto.getId(), notificationMessage);
        });
    }
}