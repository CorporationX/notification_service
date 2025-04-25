package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.event.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Компонент, обрабатывающий события лайков постов и отправляющий уведомления авторам постов.
 */
@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikePostEvent> {

    @Autowired
    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<MessageBuilder<LikePostEvent>> messageBuilders,
                             List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    /**
     * Обрабатывает событие лайка поста, отправляя уведомление автору поста.
     *
     * @param event событие лайка поста, содержащее информацию о лайке
     */
    public void handleMessage(LikePostEvent event) {
        long postAuthorId = event.getPostAuthorId();
        String message = getMessage(event, Locale.ENGLISH);
        sendNotification(postAuthorId, message);
        log.info("Successfully processed like event. Post: {}, Author: {}",
                event.getPostId(), postAuthorId);
    }
}
