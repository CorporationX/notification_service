package faang.school.notificationservice.service.message;

import faang.school.notificationservice.dto.SkillOfferEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillOfferMessageBuilder implements MessageBuilder<SkillOfferEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(SkillOfferEventDto event, Locale locale) {
        return messageSource.getMessage("skill_offer.new", null, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == SkillOfferMessageBuilder.class;
    }
}
