package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailService implements NotificationService {
    @Override
    public void send(UserClientResponseDto user, String message) {
        log.info("Email sent to user {} (email: {}): {}", user.getUsername(), user.getEmail(), message);
    }

    @Override
    public UserClientResponseDto.PreferredContact getPreferredContact() {
        return UserClientResponseDto.PreferredContact.EMAIL;
    }
}
