package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component("mentorshipAcceptedEventListener")
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> {
    public MentorshipAcceptedEventListener(List<NotificationService> notificationServices,
                                           List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders,
                                           UserServiceClient userServiceClient,
                                           ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("Handling event from " + new String(message.getChannel()));
        MentorshipAcceptedEvent event = handleEvent(message, MentorshipAcceptedEvent.class);
        String answerMessage = getMessage(event, Locale.UK);
        sendNotification(event.menteeId(), answerMessage);
        log.debug("Send notification to " + event.menteeId());
    }
}
