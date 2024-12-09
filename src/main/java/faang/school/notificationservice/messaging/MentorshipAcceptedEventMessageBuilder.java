package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedEventMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private static final String MESSAGE_KEY = "mentorship.accepted";

    private final MessageSource messageSource;

    @Override
    public Class<MentorshipAcceptedEvent> supportEventType() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[] {event.getReceiverUsername()}, locale);
    }
}
