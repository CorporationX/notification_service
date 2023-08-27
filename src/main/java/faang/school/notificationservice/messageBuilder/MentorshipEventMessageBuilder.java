package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.MentorshipEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipEventMessageBuilder implements MessageBuilder<MentorshipEventDto> {
    private final MessageSource messageSource;

    public String buildMessage(MentorshipEventDto event, Locale locale) {
        return messageSource.getMessage("mentorship.new", new Object[] {event.getRequesterId(), event.getCreatedAt()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipEventDto.class;
    }
}
