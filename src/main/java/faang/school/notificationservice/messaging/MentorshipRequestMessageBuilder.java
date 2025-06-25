package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.MentorshipRequestEvent;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipRequestEvent> {

    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipRequestEvent.class;
    }

    @Override
    public String buildMessage(MentorshipRequestEvent event, Locale locale) {
        return messageSource.getMessage("mentorshipRequest.new", new Object[]{}, locale);
    }
}
