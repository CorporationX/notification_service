package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
}
