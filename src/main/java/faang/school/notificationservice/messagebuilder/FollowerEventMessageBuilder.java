package faang.school.notificationservice.messagebuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.LocaleContextHolder;
import faang.school.notificationservice.event.follower.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final LocaleContextHolder localeContextHolder;

    @Override
    public String buildMessage(FollowerEvent event) {
        String[] userNames = {userServiceClient.getUser(event.getFolloweeId()).getUsername(),
                              userServiceClient.getUser(event.getFollowerId()).getUsername()};
        return messageSource.getMessage("follower.new", userNames, localeContextHolder.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return FollowerEvent.class;
    }
}
