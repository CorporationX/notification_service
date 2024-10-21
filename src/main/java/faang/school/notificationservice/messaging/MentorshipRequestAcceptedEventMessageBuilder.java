package faang.school.notificationservice.messaging;

import faang.school.notificationservice.messaging.dto.MentorshipRequestAcceptedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class MentorshipRequestAcceptedEventMessageBuilder implements MessageBuilder<MentorshipRequestAcceptedDto> {
    private static final String MENTORSHIP_REQUEST_ACCEPTED = "mentorship_request.accepted";

    private final MessageSource messageSource;

    @Override
    public Class<MentorshipRequestAcceptedDto> getInstance() {
        return MentorshipRequestAcceptedDto.class;
    }

    @Override
    public String buildMessage(MentorshipRequestAcceptedDto event, Locale locale) {
        Object[] args = {event.getRequestId(), event.getReceiverId()};
        return messageSource.getMessage(MENTORSHIP_REQUEST_ACCEPTED, args, locale);
    }
}
