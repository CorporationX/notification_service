package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.SkillOfferedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillOfferedEventMessageBuilder implements MessageBuilder<SkillOfferedEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(SkillOfferedEvent event, Locale locale) {
        Object[] args = new Object[]{event.skillId()};
        return messageSource.getMessage("skill.offered", args, locale);
    }
}
