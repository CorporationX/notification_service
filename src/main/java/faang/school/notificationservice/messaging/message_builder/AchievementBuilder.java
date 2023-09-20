package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.AchievementServiceClient;
import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.UserAchievementEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementBuilder extends AbstractMessageBuilder
        implements MessageBuilder<UserAchievementEvent> {
    private final AchievementServiceClient achievementServiceClient;

    @Autowired
    public AchievementBuilder(UserServiceClient userServiceClient, MessageSource messageSource,
                              AchievementServiceClient achievementServiceClient) {
        super(userServiceClient, messageSource);
        this.achievementServiceClient = achievementServiceClient;
    }

    @Override
    public String buildMessage(UserAchievementEvent eventType, Locale locale) {
        String achievementName = achievementServiceClient
                .getAchievementById(eventType.getAchievementId()).getTitle();
        return messageSource.getMessage("achievement.new", new String[]{achievementName}, locale);
    }
}
