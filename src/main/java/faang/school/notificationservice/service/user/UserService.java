package faang.school.notificationservice.service.user;

import faang.school.notificationservice.dto.UserDto;

public interface UserService {
    UserDto getUser(long id);

    UserDto getUserWithRetry(long id);
}