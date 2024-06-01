package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventMessageBuilder implements MessageBuilder<LikeEvent> {

    protected final UserServiceClient userServiceClient;
    protected final MessageSource messageSource;

    @Override
    public Class<LikeEvent> getEventType() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getAuthorLikeId());
        String postCode = "like_post.new";
        String commentCode = "like_comment.new";
        String code = null;
        Long publication = null;

        if (event.getPostId() != null) {
            code = postCode;
            publication = event.getPostId();
        } else if (event.getCommentId() != null) {
            code = commentCode;
            publication = event.getCommentId();
        }

        if (code != null) {
            return messageSource.getMessage(code, new Object[]{user.getUsername(), publication}, locale);
        } else {
            log.error("The message code for the LikeEvent event could not be found {}", event);
            throw new IllegalArgumentException("The message code for the LikeEvent event could not be found");
        }
    }
}