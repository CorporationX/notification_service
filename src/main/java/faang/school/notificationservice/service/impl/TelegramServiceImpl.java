package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final NotificationBotService notificationBotService;

    @Override
    public ResponseEntity<String> send(Long userId, String message) {
        UserDto user = new UserDto();
        user.setId(userId);

        log.info("Sending Telegram notification to userId: {}, message: {}", user.getId(), message);
        notificationBotService.sendMessage(String.valueOf(user.getId()), message);
        log.info("Notification sent successfully to userId: {}", user.getId());

        return ResponseEntity.ok("Successfully sent");
    }
}
