package faang.school.notificationservice.messaging.achievement;

import faang.school.notificationservice.client.AchievementServiceClient;
import faang.school.notificationservice.dto.achievement.AchievementDto;
import faang.school.notificationservice.event.achievement.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementReceiveMessageBuilder implements MessageBuilder<AchievementEvent> {

    private final AchievementServiceClient achievementServiceClient;
    private final MessageSource source;

    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale, Object... args) {

        Long achievementId = event.getAchievementId();
        AchievementDto achievement = achievementServiceClient.getAchievement(achievementId);

        String message = source.getMessage(
                "achievement.receive",
                new Object[]{achievement.getTitle()},
                locale
        );

        return message;
    }
}
