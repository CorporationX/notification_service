package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class SmsResponse {
    private boolean success;
    private String errorMessage;
    private String providerId;
}
