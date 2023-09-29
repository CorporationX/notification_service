package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventDto;
import faang.school.notificationservice.dto.SkillOfferEventDto;
import faang.school.notificationservice.message.LikeMessageBuilder;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.message.SkillOfferMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto>{
    @Autowired
    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto event = deserializeJson(message, LikeEventDto.class);
        String messageForNotification = getMessage(LikeMessageBuilder.class, event);
        sendNotification(event.getUserId(), messageForNotification);
    }
}
