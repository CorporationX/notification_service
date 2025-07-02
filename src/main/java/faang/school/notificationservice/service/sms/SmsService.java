package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.client.user_service.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        log.info("SMS sent to user {} (phone: {}): {}", user.getUsername(), user.getPhone(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
