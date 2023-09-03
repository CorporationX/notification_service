package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.message_builder.GoalAchievedMessageBuilder;
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
    private final GoalAchievedMessageBuilder goalAchievedMessageBuilder;
    private final Locale locale = Locale.ENGLISH;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent goalCompletedEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            String text = goalAchievedMessageBuilder.getMessage(goalCompletedEvent, locale);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
