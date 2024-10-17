package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void send(UserDto user, String message);
    UserDto.PreferredContact getPreferredContact();
}
