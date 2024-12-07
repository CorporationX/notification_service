package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnfollowMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        log.debug("Ключ сообщения: {}", "unfollow.message");
        String eventTimeFormatted = event.getEventTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", locale));
        String message = messageSource.getMessage(
            "unfollow.message",
            new Object[]{event.getFollowerId(), eventTimeFormatted},
            locale
        );
        log.debug("Сформированное сообщение: {}", message);
        return message;
    }
}
