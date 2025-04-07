package faang.school.notificationservice.controller;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification-service/sms")
@RequiredArgsConstructor
public class SmsSenderController {
    private final NotificationService smsService;

    @PostMapping("/send")
    public void sendSms(@RequestBody String message) {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        smsService.send(userDto, message);
    }

    @GetMapping("/get")
    public String someFunc() {
        int some = 0;
        some++;
        return "Hello";
    }
}
