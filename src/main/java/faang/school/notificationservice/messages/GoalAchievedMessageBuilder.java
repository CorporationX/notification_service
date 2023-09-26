package faang.school.notificationservice.messages;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AllArgsConstructor
@Component
public class GoalAchievedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(GoalCompletedEvent goalCompletedEvent, Locale locale) {
        return messageSource.getMessage("goal.completed",
                new Object[]{goalCompletedEvent.getCompletedGoalId()}, locale);
    }

    @Override
    public boolean supportsEventType(GoalCompletedEvent eventType) {
        return false;
    }
}
