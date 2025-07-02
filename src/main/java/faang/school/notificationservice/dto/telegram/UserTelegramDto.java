package faang.school.notificationservice.dto.telegram;

public record UserTelegramDto(
        Long userId,
        Long telegramChatId,
        String telegramUserName
) {
}
