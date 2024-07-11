package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffRateChangeEvent {
    private Long savingsAccountId;
    private Long tariffId;
    private BigDecimal newRate;
    private LocalDateTime changeDate;
    private Long ownerId;
}
