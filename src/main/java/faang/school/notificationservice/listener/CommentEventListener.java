package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * <p>
 * Слушатель событий комментариев.
 * Десериализует CommentEvent, формирует текст уведомления и отправляет его автору поста
 * через подходящий NotificationService с учётом предпочтений пользователя.
 * </p>
 *
 * @author agent
 * @since 13.08.2025
 */
@Component
@Slf4j
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            CommentEvent event = objectMapper.readValue(message.getBody(), CommentEvent.class);

            log.info("✅ Получен CommentEvent: {}", event);

            String messageText = getMessage(event, Locale.getDefault());
            sendNotification(event.postAuthorId(), messageText);
            log.info("📤 Уведомление отправлено пользователю {}: {}", event.postAuthorId(), messageText);

        } catch (Exception e) {
            log.error("❌ Ошибка при обработке CommentEvent", e);
            throw new RuntimeException(e);
        }
    }
}