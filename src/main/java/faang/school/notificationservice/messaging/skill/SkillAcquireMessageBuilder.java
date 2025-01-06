package faang.school.notificationservice.messaging.skill;

import faang.school.notificationservice.dto.skill.SkillAcquiredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillAcquireMessageBuilder implements MessageBuilder<SkillAcquiredEvent> {

    @Value("recommendation.new")
    private String recommendationKey;

    private final MessageSource messageSource;

    @Override
    public Class<SkillAcquiredEvent> getInstance() {
        return SkillAcquiredEvent.class;
    }

    @Override
    public String buildMessage(SkillAcquiredEvent event, Locale locale) {
        return messageSource.getMessage(recommendationKey, null, locale);
    }
}
