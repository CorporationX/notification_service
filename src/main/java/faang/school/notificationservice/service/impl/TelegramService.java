package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;
    private final TelegramUserRepository telegramUserRepository;

    @Override
    public void send(UserDto user, String message) {

        TelegramUser telegramUser = telegramUserRepository.findByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Telegram user with id = %d not found", user.getId())));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramUser.getTelegramUserId());
        sendMessage.setText(message);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException was occurred while send message to user with " +
                    "telegramUserId = {}, userName = {}", telegramUser.getTelegramUserId(), telegramUser.getUserName(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
