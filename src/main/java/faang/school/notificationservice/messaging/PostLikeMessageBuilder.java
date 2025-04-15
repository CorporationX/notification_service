package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PostLikeMessageBuilder implements MessageBuilder<PostLikeEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<PostLikeEvent> getInstance() {
        return PostLikeEvent.class;
    }

    @Override
    public String buildMessage(PostLikeEvent event, Locale locale) {
        return messageSource.getMessage(
                "post-like-message.new",
                new Object[]{event.getPostId(), event.getLikeAuthorId()},
                locale
        );
    }
}
