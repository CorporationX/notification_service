package faang.school.notificationservice.controller;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;

    @PostMapping("/notifications/{userId}/mail")
    public void sendNotificationToMail(@NotEmpty @PathVariable Long userId,@NotEmpty @RequestBody String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        emailService.send(userDto, message);
    }
}
