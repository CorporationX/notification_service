package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent>{

    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedMessageBuilder.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage("mentorship.accepted", new Object[]{},locale);
    }
}