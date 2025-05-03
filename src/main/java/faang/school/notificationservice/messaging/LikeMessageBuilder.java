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
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> supportsEventType() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getUserId());
        return messageSource.getMessage("like.post", new Object[]{user.getUsername(), event.getPostId()}, locale);
    }
}
