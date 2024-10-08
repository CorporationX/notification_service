package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompletedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(GoalCompletedEventDto event, Locale locale) {
        return messageSource.getMessage("goal.completed", new Object[]{event.userId()}, locale);
    }
}