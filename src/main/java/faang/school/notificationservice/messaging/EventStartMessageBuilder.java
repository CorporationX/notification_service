package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventDto, Locale> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventDto context, Locale locale) {
        return messageSource.getMessage(
                "event.start",
                new Object[]{context.getAttendee().getUsername(), context.getTitle()},
                locale) + getEndOfMessage(context.getTimeTillStart());
    }

    private String getEndOfMessage(long delay) {
        if (delay >= TimeUnit.DAYS.toMillis(1)) {
            return " in a day!";
        } else if (delay >= TimeUnit.HOURS.toMillis(5)) {
            return " in five hours!";
        } else if (delay >= TimeUnit.HOURS.toMillis(1)) {
            return " in an hour!";
        } else if (delay >= TimeUnit.MINUTES.toMillis(10)) {
            return " in ten minutes!";
        } else {
            return " now!";
        }
    }
}
