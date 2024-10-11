package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.comment.CommentEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class NewCommentEventListener extends AbstractEventListener<CommentEventDto> implements MessageListener {

    public NewCommentEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<CommentEventDto>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Got message, handle it");
        handleEvent(message, CommentEventDto.class, event -> {
            UserDto notifierUser = userServiceClient.getUser(event.getAuthorId());
            Locale userPreferedLocale = notifierUser.getLocale() != null ? notifierUser.getLocale() : Locale.ENGLISH;
            String text = getMessage(event, userPreferedLocale);
            sendNotification(notifierUser, text);
        });
        log.info("Finish handle message");
    }
}
