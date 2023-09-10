package faang.school.notificationservice.sender;

import faang.school.notificationservice.dto.user.UserDto;

public interface NotificationService {
    void send(UserDto user,String topic, String message);
    UserDto.PreferredContact getPreferredContact();
}
