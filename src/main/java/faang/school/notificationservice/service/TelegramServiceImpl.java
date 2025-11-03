package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TelegramServiceImpl implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        // Stub, to replace with real implementation.
        log.info("TELEGRAM to user {}: {}", user.getId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}