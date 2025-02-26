package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    private final static String MESSAGE_KEY = "comment.evil";

    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        Object[] args = {event.commentId(), event.postAuthorId()};
        return messageSource.getMessage(MESSAGE_KEY, args, locale);
    }
}
