package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.HashtagRemovingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class HashtagRemovingMessageBuilder implements MessageBuilder<HashtagRemovingEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return HashtagRemovingEvent.class;
    }

    @Override
    public String buildMessage(HashtagRemovingEvent event, Locale locale) {
        return messageSource.getMessage("hashtag.removing.notification",
                new Object[]{event.hashtagName()}, locale);
    }
}
