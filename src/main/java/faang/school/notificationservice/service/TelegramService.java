package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramChatIdNotFound;
import faang.school.notificationservice.service.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;

@Component
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        if (user.getTelegramChatId() == null) {
            throw new TelegramChatIdNotFound("Нельзя отправить уведомление в телеграм, так как нет id чата");
        }
        telegramBot.sendMessage(user.getTelegramChatId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return TELEGRAM;
    }
}
