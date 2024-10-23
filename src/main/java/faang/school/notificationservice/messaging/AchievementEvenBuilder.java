package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEvenBuilder implements MessageBuilder<AchievementEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        return messageSource.getMessage("achievement", new Object[]{}, locale);
    }
}
