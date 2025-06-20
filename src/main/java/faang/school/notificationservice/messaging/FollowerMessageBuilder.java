package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient serviceClient;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserDto followee = serviceClient.getUser(event.followeeId());
        UserDto follower = serviceClient.getUser(event.followerId());
        return messageSource.getMessage("follower-name.new",
                new Object[]{followee.getUsername(), follower.getUsername()}, locale);
    }
}
