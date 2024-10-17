package faang.school.notificationservice.messaging;


import faang.school.notificationservice.dto.goal.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class GoalCompletedEventBuilder implements MessageBuilder<GoalCompletedEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        return messageSource.getMessage("goal.completed", new Object[]{event.getGoalName()}, locale);
    }
}
