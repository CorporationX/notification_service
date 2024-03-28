package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.telegram.TelegramAccountService;
import faang.school.notificationservice.service.notification.telegram.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBotService telegramBot;
    private final TelegramAccountService telegramAccountService;

    @Override
    public void send(UserDto user, String message) {
        long chatId = telegramAccountService.getByUserId(user.getId()).getChatId();
        telegramBot.sendMessageTo(
                chatId,
                message
        );
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}