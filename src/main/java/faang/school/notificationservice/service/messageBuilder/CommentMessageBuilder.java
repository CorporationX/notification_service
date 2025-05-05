package faang.school.notificationservice.service.messageBuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.comment.CommentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getAuthorId());
        return messageSource.getMessage("comment.new", new Object[]{user.getUsername(), event.getPostId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return CommentEvent.class;
    }
}
