package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.CommentEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CommentEventMessageBuilder extends MessageBuilder<CommentEventDto> {
    private static final String MESSAGE = "comment.received";

    public CommentEventMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<?> getInstance() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        return buildMessage("notification.comment",
                locale,
                String.valueOf(event.getCommenterId()),
                event.getText(),
                String.valueOf(event.getPostId()));
    }
}