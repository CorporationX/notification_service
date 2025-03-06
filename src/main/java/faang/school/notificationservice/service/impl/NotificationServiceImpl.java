package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        log.info("Send notification to user: {}, message: {}", user, message);
    }
}
