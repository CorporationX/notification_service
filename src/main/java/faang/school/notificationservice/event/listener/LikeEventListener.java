package faang.school.notificationservice.event.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class LikeEventListener {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;

    @KafkaListener(topics = "like-events", groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleLikeEvent(LikeEvent event) {
        log.info("Получено событие лайка: {}", event);

        UserDto user = userServiceClient.getUser(event.getAuthorId());
        if (user == null || user.getEmail() == null) {
            log.error("Не удалось найти пользователя с ID {}", event.getAuthorId());
            return;
        }

        String message = String.format("Пользователь с ID %d лайкнул ваш пост с ID %d",
                event.getUserId(), event.getPostId());

        notificationService.send(user, message);
    }
}