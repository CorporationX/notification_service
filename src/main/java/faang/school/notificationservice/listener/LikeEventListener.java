package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationServiceSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Компонент, обрабатывающий события лайков постов и отправляющий уведомления авторам постов.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventListener {
    private final UserServiceClient userServiceClient;
    private final LikeMessageBuilder likeMessageBuilder;
    private final NotificationServiceSelector notificationServiceSelector;

    /**
     * Обрабатывает событие лайка поста, отправляя уведомление автору поста.
     *
     * @param event событие лайка поста, содержащее информацию о лайке
     */
    public void handleMessage(LikePostEvent event) {
        long postAuthorId = event.getPostAuthorId();
        UserDto postAuthor = userServiceClient.getUser(postAuthorId);

        String message = likeMessageBuilder.buildMessage(event, Locale.ENGLISH);
        notificationServiceSelector.notifyUser(postAuthor, message);
        log.info("Successfully processed like event. Post: {}, Author: {}",
                event.getPostId(), postAuthorId);
    }
}
