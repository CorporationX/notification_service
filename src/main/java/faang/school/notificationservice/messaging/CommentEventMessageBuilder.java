package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.redis.CommentEvent;
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
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        return messageSource.getMessage("comment.received", new Object[]{event.getComment()}, locale);
    }
}
