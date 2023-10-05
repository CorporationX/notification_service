package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.MentorshipOfferRequestSentDto;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(LikeEventDto likeEventDto, Locale locale) {
        return messageSource.getMessage("like_received", new Object[]{likeEventDto.getUserId()}, locale);
    }
}