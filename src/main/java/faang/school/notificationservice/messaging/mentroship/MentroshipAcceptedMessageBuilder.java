package faang.school.notificationservice.messaging.mentroship;

import faang.school.notificationservice.dto.mentorship.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentroshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    @Value("mentorship_accepted.new")
    private String mentorshipKey;

    private final MessageSource messageSource;

    @Override
    public Class<MentorshipAcceptedEvent> getInstance() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage(mentorshipKey, null, locale);
    }
}