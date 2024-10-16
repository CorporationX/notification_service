package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.AchievementServiceClient;
import faang.school.notificationservice.model.dto.AchievementDto;
import faang.school.notificationservice.model.event.AchievementEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    private final AchievementServiceClient achievementServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<AchievementEvent> getSupportedClass() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {

        long achievementId = event.achievementId();
        AchievementDto achievementDto = achievementServiceClient.getAchievement(achievementId);
        String title = achievementDto.getTitle();
        String description = achievementDto.getDescription();
        return messageSource.getMessage(
                "achievement",
                new Object[]{title, description},
                locale
        );
    }
}
