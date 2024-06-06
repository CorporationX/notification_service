package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.notification.NotificationRequest;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest request) {
        notificationService.send(request.getUser(), request.getMessage());
    }
}
