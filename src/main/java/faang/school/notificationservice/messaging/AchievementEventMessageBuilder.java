package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.AchievementEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementEventMessageBuilder extends MessageBuilder<AchievementEventDto> {
    private static final String MESSAGE = "achievement.received";

    public AchievementEventMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<?> getInstance() {
        return AchievementEventDto.class;
    }

    @Override
    public String buildMessage(AchievementEventDto event, Locale locale) {
        return buildMessage(MESSAGE, locale, event.getTitle(), event.getDescription());
    }
}
