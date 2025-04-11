package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.like.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        String message = messageSource.getMessage("like.new", new Object[]{event.getPostId(), event.getUserId()}, locale);
        return message;
    }
}
