package faang.school.notificationservice.dto.sms;

import lombok.Builder;

@Builder
public class SmsDto {
    private String number;
    private String destination;
    private String text;

    @Override
    public String toString() {
        return String.format(
                "{\"number\":\"%s\",\"destination\":\"%s\",\"text\":%s}",
                number, destination, text
        );
    }
}
