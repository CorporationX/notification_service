package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventNotificationService implements NotificationService{

    UserServiceClient userServiceClient;

    @Override
    public void send(Long id, String message) {
        UserDto user = userServiceClient.getById(id);
        log.info("{} {} - {}", id, user.getUsername(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
