package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    private static final String KEY = "comment.received";
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent e, Locale locale) {
        Object[] args = {e.postId(), e.authorId(), e.content()};
        return messageSource.getMessage(KEY, args, locale);
    }
}
