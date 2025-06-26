package faang.school.notificationservice.dto.telegram;

public record UserTelegramDto(
        Long id,
        Long telegramChatId,
        String telegramUserName
) {
}
