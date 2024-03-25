package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {
    public MentorshipOfferedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                          List<NotificationService> notificationServices, List<MessageBuilder> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage( Message message, byte[] pattern) {
        MentorshipOfferedEvent event = getEvent(message, MentorshipOfferedEvent.class);
        log.info("Start processing an incoming event - {}", event);

        UserDto userReceiver = userServiceClient.getUser(event.getMentorId());
        UserDto userRequester = userServiceClient.getUser(event.getAuthorId());
        event.setAuthorId(userRequester.getId());
        event.setMentorId(userReceiver.getId());
        String messageToSend = getMessage(event, Locale.getDefault());
        log.info("A message - {} has been generated to send {}", messageToSend, userReceiver.getUsername());

        sendNotification(userRequester, messageToSend);
        log.info("The message: {} has been sent to {}", messageToSend, userReceiver.getUsername());
    }
}
