package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {

    @Value("follower.new")
    private String followerKey;

    private final MessageSource messageSource;

    @Override
    public Class<FollowerEvent> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        return messageSource.getMessage(followerKey, null, locale);
    }
}
