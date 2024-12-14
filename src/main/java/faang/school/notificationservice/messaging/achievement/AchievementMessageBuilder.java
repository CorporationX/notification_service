package faang.school.notificationservice.messaging.achievement;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    private final MessageSource messageSource;

    @Value("achievement.new")
    private String achievementKey;

    @Override
    public Class<AchievementEvent> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource.getMessage(achievementKey, new Object[] {event.title()}, locale);
    }
}
