package faang.school.notificationservice.messaging;


import faang.school.notificationservice.dto.CommentEventDto;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CommentMessageBuilder implements MessageBuilder<CommentEventDto> {

    @Override
    public Class<?> getInstance() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        return String.format("User %d left a comment on your post %d: \"%s\"",
                event.commentAuthorId(), event.postId(), event.commentText());
    }
}
