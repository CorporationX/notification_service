package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.UserFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserFollowerEventMessageBuilder implements MessageBuilder<UserFollowerEvent> {
    private static final String MESSAGE_KEY = "follower.new";
    private final MessageSource messageSource;

    @Override
    public Class<UserFollowerEvent> supportEventType() {
        return UserFollowerEvent.class;
    }

    @Override
    public String buildMessage(UserFollowerEvent event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{event.getFollowerId()}, locale);
    }
}
