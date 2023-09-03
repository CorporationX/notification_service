package faang.school.notificationservice.service.achievement;

import faang.school.notificationservice.dto.AchievementDto;
import faang.school.notificationservice.service.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementMessageBuilder implements MessageBuilder<AchievementDto> {

    @Override
    public String buildMessage(AchievementDto event, Locale locale) {
        return "Поздравляем. " + event.getDescription();
    }
}
