package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserAchievementEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<UserAchievementEventDto> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserAchievementEventDto event, Locale locale) {
        String followerName = userServiceClient.getUserInternal(event.getUserId()).username();
        return messageSource.getMessage("achievement.new", new Object[]{followerName, event.getAchievementName()}, locale);
    }
}
