package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<MentorshipAcceptedEventDto> getInstance() {
        return MentorshipAcceptedEventDto.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEventDto event, Locale locale) {
        return messageSource.getMessage("mentorship.accepted", new Object[]{event.getAuthorName(), event.getReceiverName()}, locale);
    }
}