package faang.school.notificationservice.event;

import lombok.Data;

import java.util.Locale;

@Data
public class EmailEvent {
    private Long userId;
    private Locale locale;
    private String to;
    private String subject;
}
