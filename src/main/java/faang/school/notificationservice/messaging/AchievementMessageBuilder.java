package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.AchievementEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent>{
    private MessageSource messageSource;

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource
                .getMessage("messages", new Object[]{event.getUserId(), event.getAchievementId(), event.getAchievementDttm()}, locale);
    }

    @Override
    public Class<AchievementEvent> supportEventType() {
        return AchievementEvent.class;
    }
}
