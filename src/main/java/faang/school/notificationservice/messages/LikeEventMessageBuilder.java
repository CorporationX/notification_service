package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
public class LikeEventMessageBuilder extends AbstractMessageBuilder<LikeEvent> implements MessageBuilder<LikeEvent> {

    public LikeEventMessageBuilder(UserServiceClient userServiceClient,
                                   MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getAuthorLikeId());
        String code = null;
        Long publication = null;

        if (event.getPostId() != null) {
            code = "like_post.new";
            publication = event.getPostId();
        } else if (event.getCommentId() != null) {
            code = "like_comment.new:";
            publication = event.getCommentId();
        }

        if (code != null) {
            return messageSource.getMessage(code, new Object[]{user.getUsername(), publication}, locale);
        } else {
            log.error("The message code for the LikeEvent event could not be found {}", event);
            throw new IllegalArgumentException("The message code for the LikeEvent event could not be found");
        }
    }

    @Override
    public Class<?> supportEventType() {
        return LikeEvent.class;
    }
}
