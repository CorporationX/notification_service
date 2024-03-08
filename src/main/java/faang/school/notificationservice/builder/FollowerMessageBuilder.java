package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent>{
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(FollowerEvent eventType, Locale locale) {
        UserDto follower = userServiceClient.getUser(eventType.getFollowerId());
        return messageSource.getMessage("follower.new", new Object[]{follower.getUsername()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return FollowerEvent.class;
    }
}
