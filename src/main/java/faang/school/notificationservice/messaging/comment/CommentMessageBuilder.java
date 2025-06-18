package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.comment.CommentNewModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentMessageBuilder implements MessageBuilder<CommentNewModel> {
    private final MessageSource messageSource;
    @Value("${spring.messages.comment.new}")
    private String code;

    @Override
    public Class<CommentNewModel> getInstance() {
        return CommentNewModel.class;
    }

    @Override
    public String buildMessage(CommentNewModel event, Locale locale) {

        Object[] args = new Object[] {
                event.getPostTitle(),
                event.getUsernameAuthorComment(),
                event.getCommentContent()
        };

        String message = messageSource.getMessage(code, args, locale);

        log.debug("Message {} was written for the user {}", message, event.getUsernameAuthorComment());

        return message;
    }
}
