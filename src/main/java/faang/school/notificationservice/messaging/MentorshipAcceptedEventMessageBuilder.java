package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedEventMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage("mentorship-accepted.new", null, locale);
    }
}
