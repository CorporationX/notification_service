package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.comment.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;
    private final UserService userService;

    @Override
    public Class<CommentEvent> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto userDto = userService.fetchUser(event.getAuthorId());
        return messageSource.getMessage("comment.new", new Object[]{userDto.getUsername()}, locale);
    }
}
