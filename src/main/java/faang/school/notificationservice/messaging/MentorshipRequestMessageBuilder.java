package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.mentorship.MentorshipRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipRequestMessageBuilder implements MessageBuilder<MentorshipRequestDto> {
    private final MessageSource messageSource;

    @Override
    public Class<MentorshipRequestDto> getInstance() {
        return MentorshipRequestDto.class;
    }

    @Override
    public String buildMessage(MentorshipRequestDto event, Locale locale) {
        String formattedDateTime = event.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return messageSource.getMessage(
                "mentorship.request",
                new Object[]{event.getRequesterId(), formattedDateTime}, locale
        );
    }
}
