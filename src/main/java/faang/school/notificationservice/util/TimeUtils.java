package faang.school.notificationservice.util;

import java.time.Duration;

public class TimeUtils {

    public static String formatTimeToEvent(Duration timeToEvent) {
        long days = timeToEvent.toDays();
        if (days > 0) {
            return days + " day(s)";
        }

        long hours = timeToEvent.toHours();
        if (hours > 0) {
            return hours + " hour(s)";
        }

        long minutes = timeToEvent.toMinutes();
        return (minutes > 0) ? minutes + " minute(s)" : "less than a minute";
    }
}

