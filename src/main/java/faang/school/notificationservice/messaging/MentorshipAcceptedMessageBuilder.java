package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipAcceptedMessageBuilder extends MessageBuilder<MentorshipAcceptedEventDto> {
    private static final String MESSAGE_SOURCE = "mentorship.accepted";

    public MentorshipAcceptedMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedEventDto.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEventDto event, Locale locale) {
        return buildMessage(MESSAGE_SOURCE, locale);
    }
}