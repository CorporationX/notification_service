package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalMessageBuilder implements MessageBuilder<GoalCompleteEvent>{

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompleteEvent.class;
    }

    @Override
    public String buildMessage(GoalCompleteEvent goal, Locale locale) {
        return messageSource.getMessage(
                "goal.complete",
                new Object[]{goal.userId(), goal.goalId()},
                locale
        );
    }
}
