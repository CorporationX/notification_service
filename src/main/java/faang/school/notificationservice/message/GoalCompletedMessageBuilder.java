package faang.school.notificationservice.message;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.GoalDto;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent>{
    private final MessageSource messageSource;

    @Override
    public String buildMessage(GoalCompletedEvent event, Locale locale) {
        return messageSource.getMessage("goal_completed.new", new Object[]{event.getUserId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return GoalCompletedEvent.class;
    }
}
