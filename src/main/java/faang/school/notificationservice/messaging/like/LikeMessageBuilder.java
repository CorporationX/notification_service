package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.event.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage("like.new", new Object[]{event.getAuthorId()}, locale);
    }
}