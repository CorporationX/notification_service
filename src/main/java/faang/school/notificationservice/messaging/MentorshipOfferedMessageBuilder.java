package faang.school.notificationservice.messaging;


import faang.school.notificationservice.event.MentorshipOfferedEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipOfferedMessageBuilder implements MessageBuilder <MentorshipOfferedEvent>{

    @Override
    public Class<?> getInstance() {
        return MentorshipOfferedEvent.class;
    }



    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        String messageTemplate = getMessageTemplate(locale);
        return String.format(messageTemplate, event.idAuthor(), event.idRequester());
    }

    private String getMessageTemplate(Locale locale) {

        if (Locale.FRENCH.equals(locale)) {
            return "Le mentorat a été proposé par %s à %s.";
        }
        if (Locale.GERMAN.equals(locale)) {
            return "%s hat ein Mentoring-Angebot an %s gemacht.";
        }

        return "%s has offered mentorship to %s.";
    }
}