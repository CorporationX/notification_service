package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.MyTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final MyTelegramBot myTelegramBot;

    @Override
    public void send(UserDto userDto, String message) {
        Long telegramUserId = userDto.getTelegramId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramUserId.toString());
        sendMessage.setText(message);

        try {
            myTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение пользователю Telegram c ID {}. "
                    + "Ошибка: {}", telegramUserId, e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Telegram user " + telegramUserId, e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}