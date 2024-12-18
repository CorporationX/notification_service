package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @PutMapping("/api/notification-service/sendmail")
    public void sendMail(@RequestBody UserDto userDto) {
        emailService.send(userDto, "Hello world!!!");
    }
}
