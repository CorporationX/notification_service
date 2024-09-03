package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> implements MessageListener {

    public MentorshipOfferedEventListener(ObjectMapper objectMapper,
                                          UserServiceClient userServiceClient,
                                          List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                          List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipOfferedEvent mentorshipOfferedEvent =
                    objectMapper.readValue(message.getBody(), MentorshipOfferedEvent.class);
            UserDto receiver = userServiceClient.getUser(mentorshipOfferedEvent.getReceiverId());
            String notificationMessage = getMessage(mentorshipOfferedEvent, Locale.UK);
            sendNotification(receiver, notificationMessage);
        } catch (IOException e) {
            log.error("Could not read event: {}", MentorshipOfferedEvent.class, e);
            throw new RuntimeException(e);
        }
    }
}
