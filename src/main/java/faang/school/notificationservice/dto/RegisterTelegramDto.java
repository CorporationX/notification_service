package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record RegisterTelegramDto(
    String chatId,
    String phone
) {
}
