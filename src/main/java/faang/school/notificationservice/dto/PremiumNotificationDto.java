package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.PremiumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremiumNotificationDto {
    private PremiumType premiumType;
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
