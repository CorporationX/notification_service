package faang.school.notificationservice.service.recommendation;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.EventService;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationRequestEventService implements EventService {

    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    public RecommendationRequestEventService(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }

    @Override
    public void apply(UserDto user, String message) {
        UserDto.PreferredContact preference = user.getPreference();
        if (preference == null) {
            throw new IllegalArgumentException("Contact preference is not valid");
        }
        log.info("Notification {} for user {} sending to preferred contact: {}", message, user.getId(), preference);
        notificationServices.get(preference).send(user, message);
    }

}
