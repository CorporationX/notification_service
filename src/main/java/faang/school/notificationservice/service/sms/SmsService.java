package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements NotificationService {
    @Override
    public void send(UserClientResponseDto user, String message) {
        log.info("SMS sent to user {} (phone: {}): {}", user.getUsername(), user.getPhone(), message);
    }

    @Override
    public UserClientResponseDto.PreferredContact getPreferredContact() {
        return UserClientResponseDto.PreferredContact.PHONE;
    }
}
