package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletionNotificationEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletionNotificationEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletionNotificationEvent event, Locale locale) {
        return messageSource.getMessage("goal.completed", new Object[]{event.getGoalTitle()}, locale);
    }
}