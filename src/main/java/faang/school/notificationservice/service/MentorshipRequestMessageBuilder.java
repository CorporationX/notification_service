package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.MentorshipRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipRequestDto> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipRequestDto eventType, Locale locale) {
        return messageSource.getMessage("mentorship_offered.request",
                new Object[]{eventType.getRequester(), eventType.getDescription()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipRequestDto.class;
    }
}
