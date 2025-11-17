package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final GoalCompletedMessageBuilder goalCompletedMessageBuilder;
    private final EmailService emailService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Message conversion resulted in error: " + e);
        }
        UserDto recipient = userServiceClient.getUser(event.userId());
        String text = goalCompletedMessageBuilder.buildMessage(event, Locale.getDefault());
        emailService.send(recipient, text);
    }
}