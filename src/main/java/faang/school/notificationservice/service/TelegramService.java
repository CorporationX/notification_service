package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Сервис отправки сообщений через Telegram Bot API.
 * Важно: бот может писать только тем пользователям,
 * которые уже написали ему /start.
 * Для полноценной работы нужно реализовать:
 * 1. Эндпоинт генерации ссылки /start с токеном
 * 2. Привязку chat_id к пользователю при первом сообщении
 * Текущий код - базовая заготовка, требующая доработки.
 */

@Service
@Slf4j
public class TelegramService implements NotificationService {

    private final TelegramClient telegramClient;

    public TelegramService(@Value("${telegram.bot.token}") String token) {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    @Retryable(value = {TelegramApiException.class}, maxAttempts = 4, backoff = @Backoff(delay = 2000))
    public void send(UserDto user, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getId()) //*Тут вместо id telegram id пользователя. Так по задаче.
                .text(message)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error when sending a message to the user {}: {}", user.getId(), e.getMessage(), e);
            throw new TelegramMessageException("Error when sending a message to the user", user.getId(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
