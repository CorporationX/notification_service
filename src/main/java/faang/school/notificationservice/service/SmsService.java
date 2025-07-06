package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        log.info("send sms to user.\nuser phone: {}\nmessage: {}", user.getPhone(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
