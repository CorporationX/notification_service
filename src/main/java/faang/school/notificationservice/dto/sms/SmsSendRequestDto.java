package faang.school.notificationservice.dto.sms;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SmsSendRequestDto {
    private final String number;
    private final String destination;
    private final String text;
}
