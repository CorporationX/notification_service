package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.UnfollowMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnfollowEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final UnfollowMessageBuilder unfollowMessageBuilder;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        log.info("Получено сообщение из Redis: {}", messageBody);

        if (messageBody == null || messageBody.isEmpty()) {
            log.warn("Получено пустое сообщение. Обработка прекращена.");
            return;
        }

        try {
            FollowerEvent unfollowerEvent = objectMapper.readValue(messageBody, FollowerEvent.class);
            log.info("Десериализовано событие: followerId={}, followeeId={}, время события={}",
                unfollowerEvent.getFollowerId(), unfollowerEvent.getFolloweeId(), unfollowerEvent.getEventTime());

            UserDto user = userServiceClient.getUser(unfollowerEvent.getFolloweeId());
            log.info("Получены данные пользователя: userId={}, email={}, предпочтение={}",
                user.getId(), user.getEmail(), user.getPreferredContact());

            String text = unfollowMessageBuilder.buildMessage(unfollowerEvent, Locale.getDefault());
            log.info("Сформировано сообщение для уведомления: {}", text);

            notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreferredContact()))
                .findFirst()
                .ifPresentOrElse(
                    service -> {
                        service.send(user, text);
                        log.info("Уведомление отправлено через сервис: {}", service.getClass().getSimpleName());
                    },
                    () -> log.warn("Не найден подходящий сервис для отправки уведомления: предпочтение пользователя = {}", user.getPreferredContact())
                );

        } catch (Exception e) {
            log.error("Ошибка обработки FollowerEvent", e);
        }
    }
}
