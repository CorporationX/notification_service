package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.GoalCompletedEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient userService;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEventDto.class;
    }

    @Override
    public String buildMessage(GoalCompletedEventDto event, Locale locale) {
        UserDto user = userService.getUser(event.userId());
        return messageSource.getMessage("goal.completed", new Object[]{user.getUsername()}, locale);
    }
}