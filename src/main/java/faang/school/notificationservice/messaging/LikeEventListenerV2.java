package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventV2;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListenerV2 extends AbstractEventListener<LikeEventV2> implements MessageListener {

    public LikeEventListenerV2(ObjectMapper objectMapper,
                               UserServiceClient userServiceClient,
                               List<MessageBuilder<LikeEventV2>> messageBuilders,
                               List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEventV2.class, likeEventV2 -> {

            String messageForSend = getMessage(likeEventV2, Locale.ITALY);
            sendNotification(likeEventV2.getPostAuthorId(), messageForSend);
        });


    }
}
