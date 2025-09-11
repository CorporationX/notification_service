package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompleteEvent;
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
 * Слушатель событий целей.
 * Десериализует GoalCompleteEvent, формирует текст уведомления и отправляет его автору поста
 * через подходящий NotificationService с учётом предпочтений пользователя.
 * </p>
 *
 * @author fomchenkoandrey
 * @since 13.08.2025
 */
@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompleteEvent> implements MessageListener {

    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<GoalCompleteEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompleteEvent goal = objectMapper.readValue(message.getBody(), GoalCompleteEvent.class);
            String testMessage = getMessage(goal, Locale.getDefault());
            sendNotification(goal.userId(), testMessage);
            log.info("\uD83D\uDCE8 Уведомление отправлено пользователю {}: {}", goal.userId(), testMessage);
        } catch (Exception e) {
            log.error("❌ Ошибка при обработке цели", e);
            throw new RuntimeException(e);
        }
    }
}
