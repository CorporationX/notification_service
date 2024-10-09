package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        if (user.getPreference() != UserDto.PreferredContact.TELEGRAM || user.getTelegramId() == null) {
            log.info("It is not possible to send a notification via Telegram to the user {} {}", user.getId(), user.getUsername());
            return;
        }

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getTelegramId()))
                .text(message)
                .build();

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending message: {}, in Telegram to user with ID: {}", user.getTelegramId(), e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
