package faang.school.notificationservice.service;


import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService{

    @Override
    public void send(Long id, String message) {
        log.info("{} - {}", id, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
