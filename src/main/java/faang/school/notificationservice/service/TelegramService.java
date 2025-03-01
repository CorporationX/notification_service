package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final NotificationBotService notificationBotService;

    @Override
    public ResponseEntity<String> send(Long userId, String message) {
        UserDto user = new UserDto();
        user.setId(userId);
        send(user, message);
        return ResponseEntity.ok("Successfully sent");
    }

    @Override
    public void send(UserDto user, String message) {
        log.info("Sending Telegram notification to userId: {}, message: {}", user.getId(), message);
        notificationBotService.sendMessage(String.valueOf(user.getId()), message);
        log.info("Notification sent successfully to userId: {}", user.getId());
    }

    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
