package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(user.getTelegramChatId());
        messageToTelegram.setText(message);

        try {
            telegramBot.execute(messageToTelegram);
            log.info("Notification successfully sent to chat = {}", user.getTelegramChatId());
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to chat = {}", user.getTelegramChatId(), e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
