package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        return messageSource.getMessage("goal.completed", new Object[]{event.goalId()}, locale);
    }
}

