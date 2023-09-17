package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferBuilder implements MessageBuilder<MentorshipOfferedEventDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipOfferedEventDto mentorshipOfferedEventDto, Locale locale) {
        return messageSource.getMessage("mentorship.new", new Object[]{mentorshipOfferedEventDto.getRequesterId()}, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == MentorshipOfferBuilder.class;
    }
}