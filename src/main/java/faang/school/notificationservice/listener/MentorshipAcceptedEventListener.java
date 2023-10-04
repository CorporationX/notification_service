package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEventDto>{
    public MentorshipAcceptedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices, List<MessageBuilder<MentorshipAcceptedEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipAcceptedEventDto event = deserializeJson(message, MentorshipAcceptedEventDto.class);
        String messageForNotification = getMessage(MessageBuilder.class, event);
        sendNotification(event.getReceiverId(), messageForNotification);
    }
}
