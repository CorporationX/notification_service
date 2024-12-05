package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikeEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return String.format("User %d liked your post %d", event.getLikeAuthorId(), event.getPostId());
    }

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }
}
