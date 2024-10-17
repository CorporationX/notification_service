package faang.school.notificationservice.config.notification;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class NotificationConfiguration {

    private final List<NotificationService> notificationServicesList;

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notificationServices() {
        return notificationServicesList.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity(),
                        (existing, replacement) -> {
                            throw new RuntimeException(
                                    "Collision in notification services map, " +
                                            "exists: " + existing.getPreferredContact() +
                                            " replacement " + replacement.getPreferredContact());
                        }));
    }
}
