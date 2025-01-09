package faang.school.notificationservice.messaging.goal;

import faang.school.notificationservice.dto.goal.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {

    @Value("goal.new")
    private String newGoalMessageKey;

    private final MessageSource messageSource;

    @Override
    public Class<GoalCompletedEvent> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        return messageSource.getMessage(newGoalMessageKey, null, locale);
    }
}
