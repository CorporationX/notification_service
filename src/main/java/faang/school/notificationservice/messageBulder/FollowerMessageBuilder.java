package faang.school.notificationservice.messageBulder;

import faang.school.notificationservice.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        String message = messageSource.getMessage("follower.new", new Object[]{event.getFollowerId()}, locale);
        return message;
    }

    @Override
    public Boolean supportsEventType(FollowerEvent event) {
        return FollowerEvent.class == event.getClass();
    }
}
