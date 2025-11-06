package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedEventMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
       return messageSource.getMessage("mentorshipRequest.new", null, locale);
    }
}