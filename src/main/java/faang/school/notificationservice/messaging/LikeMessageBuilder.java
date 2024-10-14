package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.LikeEvent;
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
        return messageSource.getMessage("like.new",
                new Object[]{event.likeAuthorId(), event.postId()}, locale);
    }
}
