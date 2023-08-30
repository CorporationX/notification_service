package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferEventDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.message.SkillOfferMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillOfferListener extends AbstractEventListener<SkillOfferEventDto> implements MessageListener {

    @Autowired
    public SkillOfferListener(ObjectMapper objectMapper,
                              UserServiceClient userServiceClient,
                              List<NotificationService> notificationServices,
                              List<MessageBuilder<SkillOfferEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferEventDto event = deserializeJson(message, SkillOfferEventDto.class);
        String messageForNotification = getMessage(SkillOfferMessageBuilder.class, event);
        sendNotification(event.getReceiverId(), messageForNotification);
    }
}
