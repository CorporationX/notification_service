package faang.school.notificationservice.service.message_builder;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        return messageSource.getMessage("mentorship_offered.request",
                new Object[]{event.getRequesterId(), event.getReceiverId()}, locale);
    }

    @Override
    public long getReceiverId(MentorshipOfferedEvent event) {
        return event.getReceiverId();
    }
}
