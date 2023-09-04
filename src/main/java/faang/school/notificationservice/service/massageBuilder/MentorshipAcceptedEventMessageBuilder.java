package faang.school.notificationservice.service.massageBuilder;

import faang.school.notificationservice.dto.mentorshiprequest.MentorshipAcceptedDto;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedEventMessageBuilder implements MessageBuilder<MentorshipAcceptedDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedDto event, Locale locale) {
        return messageSource.getMessage(
                "mentorship_accepted.event",
                new Object[]{event.getReceiverUsername()},
                locale);
    }
}
