package faang.school.notificationservice.service.user;

import faang.school.notificationservice.dto.UserDto;

public interface FeignUserService {
    UserDto getById(Long userId);
}
