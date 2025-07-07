package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikeEventDto> {
    public static final String EVENT_LIKE_POST = "event.likePost";
    public static final String EVENT_LIKE_COMMENT = "event.likeComment";

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return LikeEventDto.class;
    }

    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        String messageCode = null;
        StringBuilder message = new StringBuilder();
        if (event.getPostId() != null) {
            messageCode = EVENT_LIKE_POST;
        } else if (event.getCommentId() != null) {
            messageCode = EVENT_LIKE_COMMENT;
        }
        if (messageCode != null) {
            message.append(messageSource.getMessage(messageCode, new Object[]{event.getSenderName()}, locale));
        }
        return message.toString();
    }
}
