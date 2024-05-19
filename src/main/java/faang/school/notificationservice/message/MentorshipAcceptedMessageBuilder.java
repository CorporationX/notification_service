package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.messagebroker.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        return messageSource.getMessage("mentorship.Accepted", new Object[]{event.getIdRequestRecipient()}, locale);
    }

    @Override
    public Class<?> supportEventType() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public long getRequestAuthor(MentorshipAcceptedEvent event) {
        return event.getRequestAuthorId();
    }
}