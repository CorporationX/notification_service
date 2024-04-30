package faang.school.notificationservice.messagebuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.LocaleContextHolder;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    private final LocaleContextHolder localeContextHolder;

    @Override
    public String buildMessage(CommentEvent event) {
        UserDto commenter = userServiceClient.getUser(event.getAuthorId());
        String defaultMessage = messageSource.getMessage("comment.new", new Object[]{commenter.getUsername(), event.getCommentContent()}, localeContextHolder.getLocale());
        return messageSource.getMessage("comment.new", new Object[]{commenter.getUsername(), event.getCommentContent()}, defaultMessage, localeContextHolder.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return CommentEvent.class;
    }
}
