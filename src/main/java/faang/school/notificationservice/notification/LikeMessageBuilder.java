package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.MentorshipOfferRequestSentDto;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.service.MessageBuilder;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {

    private MessageSource messageSource;

    @Override
    public String buildMessage(LikeEventDto likeEventDto, Locale locale) {
        return messageSource.getMessage("like_received", new Object[]{likeEventDto.getUserId()}, locale);
    }
}