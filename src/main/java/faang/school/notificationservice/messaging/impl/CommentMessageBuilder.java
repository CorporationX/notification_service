package faang.school.notificationservice.messaging.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.comment.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEventDto> {

    private static final String MESSAGE_KEY = "comment.new";

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<CommentEventDto> supportEventType() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getPostCreatorId());
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{user.getUsername(),
                event.getCommentContent(), event.getPostContent()}, locale);
    }
}
