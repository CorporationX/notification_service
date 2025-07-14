package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipMessageBuilder implements MessageBuilder<MentorshipRequestDto> {

    private static final String MESSAGE_KEY = "notification.mentorship.view";

    private final MessageSource messageSource;

    @Override
    public Class<MentorshipRequestDto> getInstance() {
        return MentorshipRequestDto.class;
    }

    @Override
    public String buildMessage(MentorshipRequestDto event, Locale locale) {
        return messageSource.getMessage(
                MESSAGE_KEY,
                new Object[]{event.getReceiverId()},
                locale
        );
    }
}
