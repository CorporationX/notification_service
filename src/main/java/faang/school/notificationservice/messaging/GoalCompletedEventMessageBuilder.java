package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventMessageBuilder implements MessageBuilder<GoalCompletedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        var goalDto = userServiceClient.getGoal(event.goalId());
        var args = new Object[]{goalDto.title(), goalDto.description()};
        return messageSource.getMessage("goal.completed", args, locale);
    }
}
