package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto>{
    private final MessageSource messageSource;
    @Override
    public String buildMessage(MentorshipAcceptedEventDto event, Locale locale) {
        return messageSource.getMessage("mentorship_accepted.new", new Object[]{event.getReceiverId()}, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == MentorshipAcceptedMessageBuilder.class;
    }
}
