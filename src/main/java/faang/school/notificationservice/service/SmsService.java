package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsService implements NotificationService {
    //dummy test service for postman test notifications
    @Override
    public void send(UserServiceDto user, String message) {
      log.info("Send sms notification to user id {} name {}", user.getId(), user.getUsername());
    }

    @Override
    public UserServiceDto.PreferredContact getPreferredContact() {
        return UserServiceDto.PreferredContact.SMS;
    }
}
