package faang.school.notificationservice.dto.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SmsSendResponseDto {
    @JsonProperty("message_id")
    private long messageId;
}
