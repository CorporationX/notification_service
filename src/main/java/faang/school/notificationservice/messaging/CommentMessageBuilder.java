package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.event.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        return messageSource.getMessage("comment.add", new Object[]{event.getCommentAuthorId(), event.getPostId()}, locale);
    }

    @Override
    public Class<CommentEvent> getInstance() {
        return CommentEvent.class;
    }
}
