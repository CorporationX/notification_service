package faang.school.notificationservice.service.comment;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentEventNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        UserDto.PreferredContact preference = user.getPreference();
        if (preference == null) {
            throw new IllegalArgumentException("Contact preference is not valid");
        }
        log.info("Notification {} for user {} sending to preferred contact: {}", message, user.getId(), preference);
    }
}
