package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {

    private static final int MAX_COMMENT_TEXT_LENGTH = 100;
    private static final String MESSAGE_KEY = "comment.new";

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        String truncatedText = truncateCommentText(event.getCommentText());
        return messageSource.getMessage(
                MESSAGE_KEY,
                new Object[]{event.getPostId(), truncatedText},
                locale
        );
    }

    private String truncateCommentText(String text) {
        if (text == null) {
            return "";
        }
        if (text.length() <= MAX_COMMENT_TEXT_LENGTH) {
            return text;
        }
        return String.format("%s...", text.substring(0, MAX_COMMENT_TEXT_LENGTH));
    }

}
