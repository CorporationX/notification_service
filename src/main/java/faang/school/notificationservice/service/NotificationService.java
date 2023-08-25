package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    UserDto.PreferredContact getPreferredContact();
    void send(UserDto userDto,String messageText);
}
