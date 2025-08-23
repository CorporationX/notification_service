package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PhoneNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {}

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}