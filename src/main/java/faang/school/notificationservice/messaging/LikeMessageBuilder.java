package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.LikeEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LikeMessageBuilder extends MessageBuilder<LikeEvent> {

    public LikeMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String buildMessage(UserDto user, LikeEvent event) {
        Object[] args = {user.getUsername(), event.getCreatedAt().toLocalTime()};
        return messageSource.getMessage("post.like", args, user.getLocale());
    }

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }
}
