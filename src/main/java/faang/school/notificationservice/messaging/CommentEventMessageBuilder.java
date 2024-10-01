package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent>{

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;


    @Override
    public Class<?> getInstance() {
        return this.getClass();
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto commentAuthor = userServiceClient.getUser(event.getAuthorId());
        Object[] args = {commentAuthor.getUsername(), event.getCommentContent()};
        return messageSource.getMessage("comment.created", args, locale);
    }

    @Override
    public Class<?> supportsEvent() {
        return CommentEvent.class;
    }
}
