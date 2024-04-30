package faang.school.notificationservice.messagebuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto commenter = userServiceClient.getUser(event.getAuthorId());
        String defaultMessage = messageSource.getMessage("comment.new", new Object[]{commenter.getUsername(), event.getCommentContent()}, Locale.ENGLISH);
        return messageSource.getMessage("comment.new", new Object[]{commenter.getUsername(), event.getCommentContent()}, defaultMessage, locale);
    }

    @Override
    public Class<?> getEventType() {
        return CommentEvent.class;
    }
}
