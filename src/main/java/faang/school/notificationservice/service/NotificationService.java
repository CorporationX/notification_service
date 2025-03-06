package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface NotificationService {

    ResponseEntity<String> send(Long userId, String message);

    void send(UserDto user, String message);
}
