package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
       return messageSource.getMessage("achievement.add", new Object[]{event.getUserName(), event.getAchievementTitle()}, locale);
    }

    @Override
    public Class<AchievementEvent> getInstance() {
        return AchievementEvent.class;
    }
}
