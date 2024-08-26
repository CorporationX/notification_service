package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final CustomTelegramBot customTelegramBot;

    @Override
    public void send(UserDto user, String message) {
        if (user.getTelegramId() != null && !message.isEmpty()) {
            customTelegramBot.sendMessage(user.getTelegramId(), message);
            log.info("Message sent to user with ID {}: {}", user.getId(), message);
        } else {
            log.warn("User or message is invalid. User: {}, Message: {}", user, message);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
