package faang.school.notificationservice.message;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent>{

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource.getMessage("achievement.new", new Object[]{event.getTitle()}, locale);
    }

}
