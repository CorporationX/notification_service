package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.NotificationRequest;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.NotificationStrategyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationStrategyService notificationStrategyService;

    @PostMapping
    public void send(@Valid @RequestBody NotificationRequest request) {
        UserDto userDto = request.getUserDto();
        String message = request.getMessage();

        NotificationService notificationService = notificationStrategyService.getNotificationService(userDto);
        notificationService.send(userDto, message);
    }
}
