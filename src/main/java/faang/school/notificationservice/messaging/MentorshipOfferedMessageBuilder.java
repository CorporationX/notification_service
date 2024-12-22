package faang.school.notificationservice.messaging;


import faang.school.notificationservice.event.MentorshipOfferedEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Component
public class MentorshipOfferedMessageBuilder implements MessageBuilder <MentorshipOfferedEvent>{

    private final MessageSource messageSource;

    public MentorshipOfferedMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Class<?> getInstance() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        String messageTemplate = getMessageTemplate(locale);
        return String.format(messageTemplate, event.idAuthor(), event.idReceiver());
    }

    private String getMessageTemplate(Locale locale) {
        return messageSource.getMessage("mentorship.offered.message", null, locale);
    }
}