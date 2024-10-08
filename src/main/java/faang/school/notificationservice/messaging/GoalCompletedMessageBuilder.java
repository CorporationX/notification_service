package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.GoalDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<GoalCompletedEvent> getInstance() {
        return GoalCompletedEvent.class;
    }

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        UserDto userDto = userServiceClient.getUser(event.getUserId());
        GoalDto goalDto = userServiceClient.getGoal(event.getGoalId());
        return messageSource.getMessage("goal_completed",
                new Object[]{userDto.getUsername(), goalDto.getTitle()}, locale);
    }
}
