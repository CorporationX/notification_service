package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {

    private static final String MESSAGE_KEY = "comment.new";

    @Value("${spring.messages.max-comment-text-length}")
    private int maxCommentTextLength;

    private final MessageSource messageSource;

    @Override
    public Class<CommentEvent> getEventType() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, UserDto author, Locale locale) {
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
        if (text.length() <= maxCommentTextLength) {
            return text;
        }
        return String.format("%s...", text.substring(0, maxCommentTextLength));
    }

}
