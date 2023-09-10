package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.notification.NotificationData;
import faang.school.notificationservice.dto.redis.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEventDto> implements MessageListener {
    public MentorshipAcceptedEventListener(ObjectMapper objectMapper,
                                           List<NotificationService> notificationServices,
                                           UserServiceClient userServiceClient,
                                           List<MessageBuilder<MentorshipAcceptedEventDto>> messageBuilders) {
        super(objectMapper, notificationServices, userServiceClient, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipAcceptedEventDto eventDto = getEvent(getMessageBody(message), MentorshipAcceptedEventDto.class);

        UserDto author = userServiceClient.getUser(eventDto.getRequesterId());
        UserDto receiver = userServiceClient.getUser(eventDto.getReceiverId());
        eventDto.setAuthorName(author.getUsername());
        eventDto.setReceiverName(receiver.getUsername());

        String messageText = getMessage(eventDto, Locale.UK,
                NotificationData.builder().from(author.getUsername())
                .to(receiver.getUsername()).build());

        sendNotification(author, messageText);
        log.info("Sending notifications for event: {}", eventDto);    }
}
