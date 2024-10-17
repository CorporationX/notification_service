package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.MentorshipOfferedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {

     private final MessageSource messageSource;

     @Override
     public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
         return messageSource.getMessage(
                 "mentorship.offered",
                 new Object[]{},
                 locale);
     }
}
