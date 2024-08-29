package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        Object[] args = {event.getPostAuthorId(), event.getPostId(), event.getAuthorId(), event.getComment()};
        return messageSource.getMessage("new comment", args, locale);
    }
}
