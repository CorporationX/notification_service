package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        // Stub, to replace with real implementation.
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email is empty");
        }
        log.info("EMAIL to {}: {}", user.getEmail(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}