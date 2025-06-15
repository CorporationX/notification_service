package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record RegisterTelegramDto(
        Long chatId,
        String phone
) {
}
