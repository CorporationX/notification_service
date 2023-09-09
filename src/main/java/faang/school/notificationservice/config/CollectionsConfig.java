package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.sender.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CollectionsConfig {
    private final List<NotificationService> notificationServices;

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notifications() {
        Map<UserDto.PreferredContact, NotificationService> notifications = new HashMap<>();
        for (NotificationService i : notificationServices) {
            notifications.put(i.getPreferredContact(), i);
        }
        return notifications;
    }
}
