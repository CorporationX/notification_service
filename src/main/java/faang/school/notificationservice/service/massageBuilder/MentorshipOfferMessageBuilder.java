package faang.school.notificationservice.service.massageBuilder;

import faang.school.notificationservice.dto.MentorshipOfferRequestSentDto;
import faang.school.notificationservice.service.MessageBuilder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MentorshipOfferMessageBuilder implements MessageBuilder<MentorshipOfferRequestSentDto> {

    private MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipOfferRequestSentDto mentorship, Locale locale) {
        return messageSource.getMessage("mentorship_request.new", new Object[]{mentorship.getRequesterId()}, locale);
    }
}
