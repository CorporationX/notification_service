package faang.school.notificationservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum PremiumType {
    ONE_MONTH(new BigDecimal("10"), 1),
    THREE_MONTHS(new BigDecimal("27"), 3),
    ONE_YEAR(new BigDecimal("90"), 12);

    private BigDecimal priceInDollars;
    private int months;
}
