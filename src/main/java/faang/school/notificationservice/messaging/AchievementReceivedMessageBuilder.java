package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.achievement.AchievementEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementReceivedMessageBuilder implements MessageBuilder<AchievementEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<AchievementEventDto> getInstance() {
        return AchievementEventDto.class;
    }

    @Override
    public String buildMessage(AchievementEventDto event, Locale locale) {
        return messageSource.getMessage(
                "achievement.received",
                new Object[]{event.getTitle()}, locale
        );
    }
}
