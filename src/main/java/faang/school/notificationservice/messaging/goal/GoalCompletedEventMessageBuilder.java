package faang.school.notificationservice.messaging.goal;

import faang.school.notificationservice.dto.goal.GoalCompletedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventMessageBuilder implements MessageBuilder<GoalCompletedEventDto> {

    private static final String MESSAGE_KEY = "notification.goal.completed";

    private final MessageSource messageSource;

    @Override
    public Class<GoalCompletedEventDto> getInstance() {
        return GoalCompletedEventDto.class;
    }

    @Override
    public String buildMessage(GoalCompletedEventDto event, Locale locale) {
        return messageSource.getMessage(
                MESSAGE_KEY,
                new Object[]{event.getGoalId()},
                locale
        );
    }
}
