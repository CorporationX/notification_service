package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.message.MentorshipOfferBuilder;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipOfferedListener extends AbstractEventListener<MentorshipOfferedEventDto> {
    @Autowired
    public MentorshipOfferedListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServices,
                                     List<MessageBuilder<MentorshipOfferedEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipOfferedEventDto mentorshipOfferedEventDto = deserializeJson(message, MentorshipOfferedEventDto.class);
        String messageForNotification = getMessage(MentorshipOfferBuilder.class, mentorshipOfferedEventDto);
        sendNotification(mentorshipOfferedEventDto.getReceiverId(), messageForNotification);
    }
}
