package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisChannel;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Компонент для обработки событий о новых подписчиках из Redis.
 * <p>
 * Прослушивает Redis-канал {@code follower_event} и обрабатывает сообщения о новых подписках.
 * При получении события отправляет уведомление пользователю, на которого подписались.
 * </p>
 *
 * @see MessageListener
 * @see AbstractEventListener
 */
@Slf4j
@Component
@RedisChannel("follower_event")
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    /**
     * Конструктор компонента.
     *
     * @param objectMapper         маппер для JSON-сериализации/десериализации
     * @param userServiceClient    клиент для получения данных о пользователях
     * @param messageBuilders      список билдеров сообщений
     * @param notificationServices список сервисов для отправки уведомлений
     */
    public FollowerEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder<FollowerEventDto>> messageBuilders,
            List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    /**
     * Обрабатывает входящее сообщение из Redis.
     * <p>
     * Десериализует сообщение в {@link FollowerEventDto} и передает на обработку.
     * </p>
     *
     * @param message входящее сообщение
     * @param pattern шаблон канала (не используется)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, this::processEvent);
    }

    /**
     * Обрабатывает событие о новой подписке.
     * <p>
     * Формирует текст уведомления и отправляет его пользователю.
     * </p>
     *
     * @param event событие о новой подписке
     */
    private void processEvent(FollowerEventDto event) {
        Locale userLocale = getUserLocale(event.getFolloweeId());
        String message = getMessage(event, userLocale);
        sendNotification(event.getFolloweeId(), message);
    }
}