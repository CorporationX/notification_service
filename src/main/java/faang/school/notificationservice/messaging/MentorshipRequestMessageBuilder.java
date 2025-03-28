package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipRequestEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<MentorshipRequestEvent> supportEventType() {
        return MentorshipRequestEvent.class;
    }

    @Override
    public String buildMessage(MentorshipRequestEvent eventType, Locale locale) {
        return messageSource.getMessage("mentorship.request", new Object[]{}, locale);
    }
}
