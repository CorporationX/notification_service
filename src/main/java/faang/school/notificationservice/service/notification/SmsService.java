package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;

public class SmsService implements NotificationService {
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    @Override
    public void sendNotification(UserDto user, String text) {

    }
}
