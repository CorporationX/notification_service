package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PostLikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto authorLike = userServiceClient.getUser(event.getAuthorLikeId());
        return messageSource.getMessage("post_like", new Object[]{authorLike.getUsername()}, locale);
    }
}
