package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartDto> {
    private static final long ONE_DAY = TimeUnit.DAYS.toMillis(1);
    private static final long FIVE_HOURS = TimeUnit.HOURS.toMillis(5);
    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private static final long TEN_MINUTES = TimeUnit.MINUTES.toMillis(10);
    private final MessageSource messageSource;

    @Override
    public Class<EventStartDto> getInstance() {
        return EventStartDto.class;
    }

    @Override
    public String buildMessage(EventStartDto event, Locale locale) {
        return messageSource.getMessage(
                "event.start",
                new Object[]{event.getNotifiedAttendee().getUsername(), event.getTitle()}, locale
        ) + getEndOfMessage(event.getTimeTillStart());
    }

    private String getEndOfMessage(long delay) {
        if (delay >= ONE_DAY) {
            return " in a day!";
        } else if (delay >= FIVE_HOURS) {
            return " in five hours!";
        } else if (delay >= ONE_HOUR) {
            return " in an hour!";
        } else if (delay >= TEN_MINUTES) {
            return " in ten minutes!";
        } else {
            return " now!";
        }
    }
}
