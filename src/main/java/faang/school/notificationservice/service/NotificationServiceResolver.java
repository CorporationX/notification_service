package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationServiceResolver {

    private final List<NotificationService> services;

    public NotificationService getServiceForPreferredContact(UserDto.PreferredContact contact) {
        return services.stream()
                .filter(service -> service.getPreferredContact() == contact)
                .findFirst()
                .orElseThrow(() -> new ServiceUnavailableException(
                        "No NotificationService registered for contact: " + contact));
    }
}
