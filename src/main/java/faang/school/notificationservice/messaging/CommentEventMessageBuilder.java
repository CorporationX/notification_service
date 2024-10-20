package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        var args = new Object[] {event.username(), event.content()};
        return messageSource.getMessage("comment.received-comment", args, locale);
    }
}
