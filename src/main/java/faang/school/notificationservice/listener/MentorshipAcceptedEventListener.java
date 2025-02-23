package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.RequestMentorshipEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<RequestMentorshipEvent> implements MessageListener {

    public MentorshipAcceptedEventListener(UserServiceClient userServiceClient,
                                           ObjectMapper objectMapper,
                                           List<NotificationService> notificationServices,
                                           List<MessageBuilder<RequestMentorshipEvent>> messageBuilders) {
        super(userServiceClient, objectMapper, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RequestMentorshipEvent event = objectMapper.readValue(message.getBody(), RequestMentorshipEvent.class);
            String text = getMessage(event, Locale.ENGLISH);
            sendMessage(event.getMenteeId(), text);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
