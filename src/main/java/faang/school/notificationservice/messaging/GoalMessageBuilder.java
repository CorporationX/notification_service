package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalMessageBuilder implements MessageBuilder<GoalCompletedEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
       Object[] placeholders = {event.getGoalId(), event.getActorId()};
       return messageSource.getMessage("goal.completed", placeholders, locale);
    }
}
