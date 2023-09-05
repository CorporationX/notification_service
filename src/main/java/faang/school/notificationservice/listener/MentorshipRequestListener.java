package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.mentorship.MentorshipRequestDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipRequestListener extends AbstractEventListener<MentorshipRequestDto> {

    public MentorshipRequestListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipRequestDto>> messageBuilder,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestDto mentorshipRequestDto = readValue(message.getBody(), MentorshipRequestDto.class);
        String notification = getMessage(mentorshipRequestDto, Locale.getDefault());
        UserDto user = userServiceClient.getUser(mentorshipRequestDto.getReceiverId());
        sendNotification(user, notification);
        log.info("Sending notifications for mentorship request: {}", mentorshipRequestDto);
    }
}
