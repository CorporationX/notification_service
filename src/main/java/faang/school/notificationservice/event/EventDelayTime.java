package faang.school.notificationservice.event;

import lombok.Getter;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Getter
public enum EventDelayTime {

    NOW(0, TimeUnit.MILLISECONDS),
    TEN_MINUTES(10, TimeUnit.MINUTES),
    FIVE_MINUTES(5, TimeUnit.MINUTES);

    private static final String MESSAGE_SOURCE_GROUP_NAME = "event_delay_time";
    private final int value;
    private final TimeUnit timeUnit;

    EventDelayTime(int value, TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    public String getMessage(String param, MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(MESSAGE_SOURCE_GROUP_NAME + "." + name().toLowerCase(), new Object[]{param}, locale);
    }
}