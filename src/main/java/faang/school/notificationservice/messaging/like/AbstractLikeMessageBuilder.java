package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractLikeMessageBuilder implements MessageBuilder<LikeEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    protected abstract String getMessageCode();

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto authorLike = userServiceClient.getUser(event.getAuthorLikeId());
        Object[] args = new Object[]{event.getPostId(), authorLike.getUsername()};
        return messageSource.getMessage(getMessageCode(), args, locale);
    }
}
