package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.messages.GoalAchievedMessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalCompletedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final GoalAchievedMessageBuilder goalAchievedMessageBuilder;
    private final Locale locale = Locale.ENGLISH;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        GoalCompletedEvent goalCompletedEvent;
        try {
             goalCompletedEvent = objectMapper.readValue(messageBody, GoalCompletedEvent.class);
            String text = goalAchievedMessageBuilder.buildMessage(goalCompletedEvent, locale);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("An event from " + channel + " is received: " + goalCompletedEvent.getCompletedGoalId());
    }
}
