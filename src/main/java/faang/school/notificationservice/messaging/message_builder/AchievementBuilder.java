package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.UserAchievementEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementBuilder extends AbstractMessageBuilder
        implements MessageBuilder<UserAchievementEvent> {

    @Autowired
    public AchievementBuilder(UserServiceClient userServiceClient, MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }

    @Override
    public String buildMessage(UserAchievementEvent eventType, Locale locale) {
        String achievementName = eventType.getAchievementTitle();
        return messageSource.getMessage("achievement.new", new String[]{achievementName}, locale);
    }
}
