package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
@Component
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Autowired
    public FollowerMessageBuilder(UserServiceClient userServiceClient, MessageSource messageSource) {
        this.userServiceClient = userServiceClient;
        this.messageSource = messageSource;
    }

    @Override
    public String buildMessage(FollowerEvent event) {
        UserDto follower = userServiceClient.getUser(event.getFollower());
        UserDto followee = userServiceClient.getUser(event.getFollowee());
        Locale userLocale = Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayName().toUpperCase().equals(followee.getPreferredLocale()))
                .findFirst().orElse(null);
        log.info("Follower, followee: {}, {}", follower.getUsername(), followee.getUsername());
        return messageSource.getMessage("follower.new", new Object[]{}, userLocale);
    }

    @Override
    public Class<?> supportsEventType() {
        return FollowerEvent.class;
    }
}
