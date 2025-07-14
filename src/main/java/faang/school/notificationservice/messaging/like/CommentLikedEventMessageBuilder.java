package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.event.kafka.EventStartNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentLikedEventMessageBuilder implements MessageBuilder<CommentLikedNotificationEvent> {

    private final MessageSource messageSource;

    public Class<?> getInstance() {
        return EventStartNotificationEvent.class;
    }

    @Override
    public String buildMessage(CommentLikedNotificationEvent event, Locale locale) {
        return messageSource.getMessage("comment.liked", new Object[]{event.getCommentId()}, locale);
    }
}
