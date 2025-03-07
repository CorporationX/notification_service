package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.user.UserDto;

public interface EventService {

    void apply(UserDto user, String message);

}
