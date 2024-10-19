package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.CommentEvent;
import faang.school.notificationservice.service.MessageBuilder;
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
    public Class<CommentEvent> getSupportedClass() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {

        Long commentAuthorId = event.getAuthorId();
        UserDto userCommentAuthor = userServiceClient.getUser(commentAuthorId);
        String commentAuthorName = userCommentAuthor.getUsername();

        return messageSource.getMessage(
                "comment",
                new Object[]{commentAuthorName},
                locale
        );
    }
}
