package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final NotificationBot notificationBot;

    @Override
    public void send(UserDto user, String message) {
        if (user == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        log.info("Sending Telegram notification to userId: {}, message: {}", user.getId(), message);
        notificationBot.sendMessage(String.valueOf(user.getId()), message);
        log.info("Notification sent successfully to userId: {}", user.getId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
