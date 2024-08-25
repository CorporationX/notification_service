package faang.school.notificationservice.messaging;

import faang.school.notificationservice.listener.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Evgenii Malkov
 */
@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource.getMessage("achievement.new", new Object[]{event.getTitle(), event.getDescription()}, locale);
    }
}
