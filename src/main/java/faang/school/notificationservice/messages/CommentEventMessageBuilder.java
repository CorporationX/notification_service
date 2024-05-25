package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventMessageBuilder implements MessageBuilder<CommentEvent> {

    protected final UserServiceClient userServiceClient;
    protected final MessageSource messageSource;

    @Override
    public Class<CommentEvent> getEventType() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getAuthorOfPostId());
        Long publication = event.getCommentId();

        return messageSource.getMessage("comment.new", new Object[]{user.getUsername(), publication}, locale);
    }
}