package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.comment.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        Object[] args = {event.getPostId(), event.getCommentContent()};
        return messageSource.getMessage("comment.new", args, locale);
    }
}
