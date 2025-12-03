package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent>{

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle("message", locale);
        String messageTemplate = bundle.getString("goal.completed");

        return String.format("%s (userId=%d, goalId=%d)",
                messageTemplate,
                event.userId(),
                event.goalId());
    }
}