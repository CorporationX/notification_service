package faang.school.notificationservice.service.messageBuilders;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale userLocale) {
        return messageSource.getMessage("mentor_request.new", new Object[]{event.getAuthorId()}, userLocale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipOfferedEvent.class;
    }
}
