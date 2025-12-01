package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.PostPublishedEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TelegramPostPublishedMessageBuilder implements MessageBuilder<PostPublishedEvent> {

    @Value("${telegram.messages.max-length-visible}")
    private int maxLengthVisibleMessage;

    private final MessageSource messageSource;

    @Override
    public Class<PostPublishedEvent> getEventType() {
        return PostPublishedEvent.class;
    }

    @Override
    public String buildMessage(PostPublishedEvent event, UserDto author, Locale locale) {
        String formatedTime = String.format("%tH:%tM", event.publishedAt(), event.publishedAt());
        String visibleMessageText;
        if (event.content().length() > maxLengthVisibleMessage) {
            visibleMessageText = String.format("%s ...", event.content().substring(0, maxLengthVisibleMessage));
        } else {
            visibleMessageText = event.content();
        }
        Object[] args = {formatedTime, author.getUsername(), visibleMessageText};
        return messageSource.getMessage("post.published", args, locale);
    }
}
