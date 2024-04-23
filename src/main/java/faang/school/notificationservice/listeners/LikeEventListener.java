package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper,
                             List<NotificationService> notificationServicesList,
                             List<MessageBuilder<LikeEvent>> messageBuildersList,
                             UserServiceClient userServiceClient) {
        super(objectMapper, notificationServicesList, messageBuildersList, userServiceClient);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, likeEvent -> {
            String textMessage = getMessage(likeEvent, Locale.UK); // TODO: Тут надо бы по хорошему брать с профиля пользователя локаль,
            // а это надо в юзер сервис идти + в таблице добавить столбец для локали
            sendNotification(likeEvent.getAuthorLikeId(), textMessage);
        });
    }
}
