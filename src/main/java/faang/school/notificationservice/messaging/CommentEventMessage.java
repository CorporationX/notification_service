package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.ProjectServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserEventDto;
import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.event.CommentEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessage implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    private final String MESSAGE_KEY = "comment.new";

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserEventDto authorComment = userServiceClient.getUserForEvent(event.getAuthorCommentId());
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{authorComment.getUsername(), event.getPostId()
        }, locale);
    }
}
