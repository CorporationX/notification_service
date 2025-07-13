package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentCreationMessageBuilder implements MessageBuilder <CommentCreationNotificationEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentCreationNotificationEvent.class;
    }

    @Override
    public String buildMessage(CommentCreationNotificationEvent event, Locale locale) {
        String messageTemplateCode = "comment.created";
        return messageSource.getMessage(
                messageTemplateCode,
                new Object[]{event.getCommentAuthorUserName(), event.getShortContent()},
                locale);
    }
}
