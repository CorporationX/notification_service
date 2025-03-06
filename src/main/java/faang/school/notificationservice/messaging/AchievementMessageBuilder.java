package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    private static final String MESSAGE_KEY = "achievement.unlocked";
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return AchievementMessageBuilder.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{event.getAchievement()}, locale);
    }
}
