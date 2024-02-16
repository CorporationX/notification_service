package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.GoalCompletedEvent;
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
    public String buildMessage(GoalCompletedEvent event, String username) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("goal_completed.new", new String[]{username}, locale);
    }
}
