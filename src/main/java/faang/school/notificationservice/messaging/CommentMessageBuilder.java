package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        String nameCommentAuthor = userServiceClient.getUser(event.getCommentAuthorId()).getUsername();
        return messageSource.getMessage("comment.new",new Object[]{nameCommentAuthor},locale);
    }
}
