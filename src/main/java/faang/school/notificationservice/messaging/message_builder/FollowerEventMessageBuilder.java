package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.messaging.listeners.FollowerEventListener.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        // Uses the message from messages.properties: follower.new = "Congrats! You've got a new follower!"
        return messageSource.getMessage("follower.new", null, locale);
    }
}