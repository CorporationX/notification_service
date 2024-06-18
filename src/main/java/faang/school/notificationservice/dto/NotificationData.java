package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationData {
    private String follower;
    private String followee;
}