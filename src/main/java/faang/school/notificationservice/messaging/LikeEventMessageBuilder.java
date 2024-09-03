package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventV2;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikeEventV2> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(LikeEventV2 eventType, Locale locale) {
        UserDto likeAuthor = userServiceClient.getUser(eventType.getLikeAuthorId());
        return messageSource.getMessage("like_event.new", new Object[]{
                        likeAuthor.getUsername(),
                        eventType.getLikedPostId(),
                        eventType.getLikeDateTime()
                }
                , locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return LikeEventV2.class;
    }
}
