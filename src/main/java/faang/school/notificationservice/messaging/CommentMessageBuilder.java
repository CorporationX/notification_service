package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.NotificationData;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale, NotificationData notificationData) {
        return messageSource.getMessage("comment.new", new Object[]{notificationData.getFollower()}, locale);
    }
}
