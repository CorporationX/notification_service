package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[] {event.followerId()}, locale);
    }
}
