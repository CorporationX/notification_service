package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.event.GoalCompletedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventMessageBuilder implements MessageBuilder<GoalCompletedEventDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(GoalCompletedEventDto event, Locale locale) {
        var args = new Object[]{userServiceClient.getGoal(event.goalId()).title()};
        return messageSource.getMessage("goal.completed", args, locale);
    }
}
