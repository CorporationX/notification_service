package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedEventDto eventType, Locale locale) {
        return messageSource.getMessage("mentorship.accepted", new Object[]{eventType.getReceiverName()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
                return MentorshipAcceptedEventDto.class;
    }
}
