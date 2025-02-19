package faang.school.notificationservice.service.recommendation;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationRequestNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        UserDto.PreferredContact preference = user.getPreference();
        if (preference == null) {
            throw new IllegalArgumentException("Contact preference is not valid");
        }
        log.info("Notification {} for user {} sending to preferred contact: {}", message, user.getId(), preference);
    }

}
