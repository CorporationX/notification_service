package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferBuilder {
    private final MessageSource messageSource;
    @Value("${message.mentorship.new}")
    private String key = "mentorship.new";

    public String createMessage(MentorshipOfferedEventDto mentorshipOfferedEventDto) {
        Locale locale = mentorshipOfferedEventDto.getLocale();
        String message = messageSource.getMessage(key, null, locale);
        return message + " user with id" + mentorshipOfferedEventDto.getRequesterId() + " at " + mentorshipOfferedEventDto.getTimestamp();
    }
}
