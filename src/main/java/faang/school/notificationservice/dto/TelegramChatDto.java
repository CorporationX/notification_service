package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class TelegramChatDto {
    private Long id;
    private Long userId;
    private Long chatId;
}
