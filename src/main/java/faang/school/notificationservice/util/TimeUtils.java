package faang.school.notificationservice.util;

import java.time.Duration;

public class TimeUtils {

    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    public static String formatTimeToEvent(Duration timeToEvent) {
        long days = timeToEvent.toDays();
        if (days > 0) {
            return formatTimeUnit(days, DAY);
        }

        long hours = timeToEvent.toHours();
        if (hours > 0) {
            return formatTimeUnit(hours, HOUR);
        }

        long minutes = timeToEvent.toMinutes();
        return (minutes > 0) ? formatTimeUnit(minutes, MINUTE) : "less than a minute";
    }

    private static String formatTimeUnit(long value, String unit) {
        return value + " " + unit + (value == 1 ? "" : "s");
    }
}

