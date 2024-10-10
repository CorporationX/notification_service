package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService<M> {
    void send(UserDto user, M message);
    UserDto.PreferredContact getPreferredContact();

}
