package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikeEventDto> {

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
            messageCode = "event.likePost";
        } else if (event.getCommentId() != null) {
            messageCode = "event.likeComment";
        }
        if (messageCode != null) {
            message.append(messageSource.getMessage(messageCode, new Object[]{event.getSenderName()}, locale));
        }
        return message.toString();
    }
}
