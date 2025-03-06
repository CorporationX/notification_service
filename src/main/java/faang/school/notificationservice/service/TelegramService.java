package faang.school.notificationservice.service;

import org.springframework.http.ResponseEntity;

public interface TelegramService {
  ResponseEntity<String> send(Long userId, String message);
}
