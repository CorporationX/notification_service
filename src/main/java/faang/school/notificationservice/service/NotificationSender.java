package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationMethodNotSupportedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static faang.school.notificationservice.messages.ErrorMessages.NOTIFICATION_METHOD_IS_NOT_SUPPORTED;

@Service
@RequiredArgsConstructor
public class NotificationSender {
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    public void sendNotification(String message, Long userId) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new NotificationMethodNotSupportedException(
                        NOTIFICATION_METHOD_IS_NOT_SUPPORTED.formatted(userDto.getPreference())))
                .send(userDto, message);
    }
}
