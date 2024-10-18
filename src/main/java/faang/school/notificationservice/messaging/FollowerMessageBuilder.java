package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class FollowerMessageBuilder extends MessageBuilder<FollowerEvent> {

    public FollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String buildMessage(UserDto user, FollowerEvent event) {
        Object[] args = {user.getUsername(), event.getEventTime().toLocalTime()};
        return messageSource.getMessage("follower.new", args, user.getLocale());
    }

    @Override
    public Class<FollowerEvent> getInstance() {
        return FollowerEvent.class;
    }
}
