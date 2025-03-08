package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceFactory {
    private final Map<UserProfileDto.PreferredContact, NotificationService> servicesMap;

    @Autowired
    public NotificationServiceFactory(List<NotificationService> services) {
        this.servicesMap = services.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        Function.identity(),
                        (existing, replacement) -> {
                            throw new IllegalStateException("Duplicate service for preference: " + existing.getPreferredContact());
                        }
                ));
    }

    public NotificationService getService(UserProfileDto.PreferredContact preference) {
        if (preference == null) {
            throw new IllegalArgumentException("User's preferred contact method is not specified");
        }
        log.debug("Selecting service for preference: {}", preference);
        return Optional.ofNullable(servicesMap.get(preference))
                .orElseThrow(() -> new IllegalArgumentException("No service found for preference: " + preference));
    }
}