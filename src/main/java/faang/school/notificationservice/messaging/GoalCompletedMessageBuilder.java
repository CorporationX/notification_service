package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {

    private static final String GOAL_COMPLETED_CODE = "goal.completed";
    private static final String USERNAME_PLACEHOLDER = "%s";

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        String goalTitle = event.goalTitle();
        Object[] args = {USERNAME_PLACEHOLDER, goalTitle};
        return messageSource.getMessage(GOAL_COMPLETED_CODE, args, locale);
    }
}
