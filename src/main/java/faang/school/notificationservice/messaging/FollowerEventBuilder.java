package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final Locale defaultLocale = Locale.ENGLISH;

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        try {
            return messageSource.getMessage(
                    "follower.new",
                    new Object[]{follower.getUsername()},
                    locale != null ? locale : defaultLocale
            );
        } catch (NoSuchMessageException e) {
            log.error("Message not found for code 'follower.new' and locale '{}'", locale, e);
            throw new RuntimeException("Message not found for code 'follower.new' and locale '" +
                    (locale != null ? locale : defaultLocale) + "'", e);
        }
    }

    @Override
    public Class<?> supportsEventType() {
        return FollowerEvent.class;
    }
}

