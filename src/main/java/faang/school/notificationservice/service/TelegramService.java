package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramServiceBot;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramMessageSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramServiceBot telegramServiceBot;

    @Override
    public void send(UserDto user, String message) {
        try {
            telegramServiceBot.sendMessage(user.getId(), message);
            log.info("Telegram notification sent to user: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to send telegram notification to user: {}",
                    user.getId(), e);
            throw new TelegramMessageSendException("Failed to send telegram message", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
