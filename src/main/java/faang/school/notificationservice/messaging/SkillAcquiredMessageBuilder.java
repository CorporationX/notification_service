package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.SkillAcquiredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillAcquiredMessageBuilder implements MessageBuilder<SkillAcquiredEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(SkillAcquiredEvent event, Locale locale) {
        Object[] args = new Object[]{event.skillId()};
        return messageSource.getMessage("skill.acquired", args, locale);
    }
}
