package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Слушатель (подписчик) топика с запросами рекомендаций
 *
 * @author Linempy
 * @since 13.08.2025
 */
@Slf4j
@Component
public class RecommendationRequestedEventListener extends AbstractMessageListener<RecommendationRequestedEvent>
        implements MessageListener {

    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userClient,
                                                List<NotificationService> services,
                                                List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders) {
        super(objectMapper, userClient, services, messageBuilders);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleMessage(message,
                RecommendationRequestedEvent.class,
                event -> {
                    // TODO: локаль захардкожена
                    String text = getMessage(event, Locale.US);
                    sendMessage(event.receiverId(), text);
                });
    }
}