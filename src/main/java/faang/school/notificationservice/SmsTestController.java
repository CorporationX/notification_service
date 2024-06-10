package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsTestController {
    private final SmsService smsService;

    @PostMapping("/sms")
    public void sendSms(@RequestParam String message) {
        UserDto user = new UserDto();
        user.setPhone("88005553535");

        smsService.send(user, message);
    }
}
