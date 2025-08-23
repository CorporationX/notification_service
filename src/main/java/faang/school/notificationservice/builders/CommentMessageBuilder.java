package faang.school.notificationservice.messaging;

import faang.school.notificationservice.events.CommentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        String template = messageSource.getMessage(
                "notification.comment.template",
                new Object[]{
                        event.postId(),
                        truncateText(event.commentText(), 100)
                },
                "Someone commented on your post #{0}: \"{1}\"",
                locale
        );

        return template;
    }

    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}