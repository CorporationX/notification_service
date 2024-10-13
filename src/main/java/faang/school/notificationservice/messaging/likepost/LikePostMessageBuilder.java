package faang.school.notificationservice.messaging.likepost;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikePostMessageBuilder implements MessageBuilder<LikePostEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<LikePostEvent> getSupportedClass() {
        return LikePostEvent.class;
    }

    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {

        Long likeAuthorId = event.getLikeAuthorId();
        UserDto userLikeAuthor = userServiceClient.getUser(likeAuthorId);
        String likeAuthorName = userLikeAuthor.getUsername();

        return messageSource.getMessage(
                "like.post",
                new Object[]{likeAuthorName},
                locale
        );
    }
}
