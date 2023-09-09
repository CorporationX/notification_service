package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<MentorshipAcceptedEventDto> getInstanceClass() {
        return MentorshipAcceptedEventDto.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEventDto event, Locale locale) {
        return messageSource.getMessage("mentorship.accepted", new Object[]{event.getAuthorName(), event.getReceiverName()}, locale);
    }
}