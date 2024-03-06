package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipOfferedEvent eventType, Locale locale) {
        return messageSource.getMessage("mentorship_offered.request",
                new Object[]{eventType.getRequesterId(), eventType.getReceiverId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipOfferedEvent.class;
    }
}
