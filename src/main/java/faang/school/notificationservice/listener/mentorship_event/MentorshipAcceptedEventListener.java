package faang.school.notificationservice.listener.mentorship_event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.listener.AbstractListener;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipAcceptedEventListener extends AbstractListener<MentorshipAcceptedEventDto> {

    public MentorshipAcceptedEventListener(ObjectMapper objectMapper,
                                           UserServiceClient userServiceClient,
                                           List<MessageBuilder<MentorshipAcceptedEventDto>> messageBuilders,
                                           List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipAcceptedEventDto eventDto = readValue(message.getBody(), MentorshipAcceptedEventDto.class);
        UserDto author = userServiceClient.getUser(eventDto.getAuthorId());
        UserDto receiver = userServiceClient.getUser(eventDto.getReceiverId());
        eventDto.setAuthorName(author.getUsername());
        eventDto.setReceiverName(receiver.getUsername());
        String messageText = getMessage(eventDto, Locale.UK);
        log.info("Sending messageText: {}", messageText);
        sendNotification(author, messageText);
        log.info("Sending notifications for event: {}", eventDto);
    }
}