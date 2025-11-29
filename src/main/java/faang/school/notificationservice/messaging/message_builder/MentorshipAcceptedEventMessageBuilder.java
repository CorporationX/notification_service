package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.event.mentorship.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedEventMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage("mentorshipAccept.new", null, locale);
    }
}