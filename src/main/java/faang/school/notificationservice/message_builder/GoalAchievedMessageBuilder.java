package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@AllArgsConstructor
public class GoalAchievedMessageBuilder implements MessageBuilder<GoalCompletedEvent>{

private final MessageSource messageSource;

    @Override
    public String getMessage(GoalCompletedEvent goalCompletedEvent, Locale locale) {
        return messageSource.getMessage("goal.completed",
                new Object[]{goalCompletedEvent.getCompletedGoalId()}, locale);
    }
}
