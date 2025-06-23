package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.config.messaging.CommentMessagesProperties;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.kafka.comment.CommentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentMessageBuilder implements MessageBuilder<CommentMessage> {

    private final MessageSource messageSource;
    private final CommentMessagesProperties messageProperties;

    @Override
    public Class<CommentMessage> getInstance() {
        return CommentMessage.class;
    }

    @Override
    public String buildMessage(CommentMessage event, Locale locale) {

        Object[] args = new Object[] {
                event.getPostTitle(),
                event.getUsernameAuthorComment(),
                event.getCommentContent()
        };

        String message = messageSource.getMessage(messageProperties.getNewLabel(), args, locale);

        log.info("Message {} was written for the user {}", message, event.getUsernameAuthorComment());

        return message;
    }
}
