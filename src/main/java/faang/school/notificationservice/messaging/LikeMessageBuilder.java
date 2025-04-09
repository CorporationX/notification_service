package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikePostEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return LikePostEvent.class;
    }

    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {
        UserDto postAuthor = userServiceClient.getUser(event.getPostAuthorId());
        UserDto liker = userServiceClient.getUser(event.getLikerId());

        return messageSource.getMessage("postLike.new",
                new Object[]{
                        postAuthor.getUsername(),
                        liker.getUsername()},
                locale);
    }
}
