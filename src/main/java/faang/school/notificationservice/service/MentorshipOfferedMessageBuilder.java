package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEventDto> {

    private MessageSource messageSource;
    @Override
    public String buildMessage(MentorshipOfferedEventDto eventType, Locale locale) {
        return messageSource.getMessage("mentorship_offered.request", new Object[]{eventType.getRequesterId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipOfferedEventDto.class;
    }
}
