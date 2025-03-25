package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.service.NotificationService;

public class SmsService implements NotificationService {
    @Override
    public void send(UserNotificationDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
