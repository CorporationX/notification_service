package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramBotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramBotService telegramBot;

    @Override
    public void send(UserDto user, String message) {
        try {
            long chatId = user.getId();
            telegramBot.sendMessageTo(chatId, message);
        } catch (EntityNotFoundException e) {
            log.error("EntityNotFoundException, chat id is not define to user with id = {}", user.getId(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}