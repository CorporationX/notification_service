package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MessageCode;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto commentator = userServiceClient.getUser(event.commentAuthorId());
        Object[] args = new Object[]{commentator.getUsername(), event.postId(), event.content()};
        return messageSource.getMessage(MessageCode.COMMENT_NEW.getCode(), args, locale);
    }
}
