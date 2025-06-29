package faang.school.notificationservice.dto.sms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SmsDto {
    private String number;
    private String destination;
    private String text;
}
