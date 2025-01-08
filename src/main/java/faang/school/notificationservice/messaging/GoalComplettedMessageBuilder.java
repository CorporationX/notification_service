package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalComplettedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        return messageSource.getMessage("goal.completed", new Object[]{event.getGoalId()}, locale);
    }
}
