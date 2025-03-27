package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record StockAlertDto(
    Long userId,
    String username,
    String email
) {
}
