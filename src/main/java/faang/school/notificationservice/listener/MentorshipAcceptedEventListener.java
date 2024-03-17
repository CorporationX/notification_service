package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEventDto> {
    public MentorshipAcceptedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices, List<MessageBuilder> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipAcceptedEventDto event = getEvent(message, MentorshipAcceptedEventDto.class);
        log.info("Start processing an incoming event - {}", event);

        UserDto userRequester = userServiceClient.getUser(event.getRequesterId());
        UserDto userReceiver = userServiceClient.getUser(event.getReceiverId());
        event.setRequesterName(userRequester.getUsername());
        event.setReceiverName(userReceiver.getUsername());
        String messageToSend = getMessage(event, Locale.getDefault());
        log.info("A message - {} has been generated to send {}", messageToSend, userRequester.getUsername());

        sendNotification(userRequester, messageToSend);
        log.info("The message: {} has been sent to {}", messageToSend, userRequester.getUsername());
    }
}