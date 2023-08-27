package faang.school.notificationservice.messageBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipEventMessageBuilder implements MessageBuilder {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(Locale locale, String userName) {
        return messageSource.getMessage("mentorship.event.message", new Object[]{userName}, locale);
    }
}
