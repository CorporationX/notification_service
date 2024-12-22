package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationSender {

    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;

    public void sendNotification(Long userId, String message) {
        UserDto user = getUserDto(userId);
        NotificationService service = notificationServices.stream()
                .filter(s -> s.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported contact preference: " + user.getPreference()));
        service.send(user, message);
    }

    UserDto getUserDto(Long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException e) {
            log.error("Failed to retrieve user with ID: {}", userId, e);
            throw new EntityNotFoundException("User not found with ID: " + userId, e);
        }
    }
}