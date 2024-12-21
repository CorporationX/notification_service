package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        validateTelegramUserParams(user);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(message)
                .build();
        telegramBot.send(sendMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    private void validateTelegramUserParams(UserDto user) {
        if(!user.getPreference().equals(UserDto.PreferredContact.TELEGRAM)) {
            log.error("Telegram is not the preferred platform for sending notifications for user: {}", user.getId());
            throw new IllegalArgumentException(String.format("Telegram is not the preferred platform for sending notifications for user: %s", user.getId()));
        }
        if(Objects.isNull(user.getTelegramId())) {
            log.error("Unable to send notification to telegram. TelegramID is null for user: {}", user.getId());
            throw new IllegalArgumentException(String.format("Unable to send notification to telegram. TelegramID is null for user: %s", user.getId()));
        }
    }
}