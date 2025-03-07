package faang.school.notificationservice.service.comment;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.EventService;
import faang.school.notificationservice.service.notification.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentEventService implements EventService {

    private final List<NotificationService> notificationServices;
    private Map<UserDto.PreferredContact, NotificationService> notificationServicesByPrefferedContact;

    @PostConstruct
    public void init() {
        this.notificationServicesByPrefferedContact = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }

    @Override
    public void apply(UserDto user, String message) {
        UserDto.PreferredContact preference = user.getPreference();
        if (preference == null) {
            throw new IllegalArgumentException("Contact preference is not valid");
        }
        log.info("Notification {} for user {} sending to preferred contact: {}", message, user.getId(), preference);
        notificationServicesByPrefferedContact.get(preference).send(user, message);
    }
}
