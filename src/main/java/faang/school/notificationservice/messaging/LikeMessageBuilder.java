package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage("like.add", new Object[]{event.getLikeAuthorId(), event.getPostId()}, locale);
    }

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }
}
