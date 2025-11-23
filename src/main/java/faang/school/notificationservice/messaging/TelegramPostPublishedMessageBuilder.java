package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.PostPublishedEvent;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TelegramPostPublishedMessageBuilder implements MessageBuilder<PostPublishedEvent> {

    private static final String MESSAGE_FORMAT = "%tH:%tM -> %s опубликовал(-а) новый пост%n%s";
    private static final int MAX_LENGTH_VISIBLE_MESSAGE = 17;

    @Override
    public Class<PostPublishedEvent> getEventType() {
        return PostPublishedEvent.class;
    }

    @Override
    public String buildMessage(PostPublishedEvent event, UserDto author, Locale locale) {
        String visibleMessageText;
        if (event.content().length() > MAX_LENGTH_VISIBLE_MESSAGE) {
            visibleMessageText = String.format("%s ...", event.content().substring(0, MAX_LENGTH_VISIBLE_MESSAGE));
        } else {
            visibleMessageText = event.content();
        }
        return String.format(locale,
                MESSAGE_FORMAT,
                event.publishedAt(),
                event.publishedAt(),
                author.getUsername(),
                visibleMessageText);
    }
}
