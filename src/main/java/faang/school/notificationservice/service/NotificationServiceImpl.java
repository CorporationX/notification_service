package faang.school.notificationservice.service;


import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        log.info("Notification to {} via {}: {}", user.getId(), getPreferredContact(), message);
    }

    public void send(Long userId, String message) {
        UserDto user = new UserDto();
        user.setId(userId);
        send(user, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
