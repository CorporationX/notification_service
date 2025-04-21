package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto>{
    private static final String MESSAGE_SOURCE = "mentorship.accepted";
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedEventDto.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEventDto event, Locale locale) {
        return messageSource.getMessage(MESSAGE_SOURCE, new Object[]{},locale);
    }
}