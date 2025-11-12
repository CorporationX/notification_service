package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeLeft {
    START(0, "event.start"),
    MINUTES_10(10, "event.before.10minutes"),
    HOUR_1(60, "event.before.1hour"),
    HOURS_5(300, "event.before.5hour"),
    HOURS_24(1440, "event.before.24hour");
    ;

    private final int minutes;
    private final String messageKey;
}
