package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.client.telegram.bot.CorpXTelegramBot;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static faang.school.notificationservice.utils.ConstantSettings.NO_CHAT_ID_ERROR;

@RequiredArgsConstructor
@Slf4j
@Service
public class TelegramNotificationService implements NotificationService {

    private final CorpXTelegramBot telegramBot;
    private final TelegramUserService telegramUserService;

    @Override
    public void send(UserDto user, String message) {
        telegramUserService.getTelegramChatIdByUserId(user.getId())
                .ifPresentOrElse(chatId -> {
                            telegramBot.sendMessage(chatId, message);
                            log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
                        },
                        () -> log.error("Failed to send telegram notification! Reason: {}", String.format(NO_CHAT_ID_ERROR, user.getId()))
                );

    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}