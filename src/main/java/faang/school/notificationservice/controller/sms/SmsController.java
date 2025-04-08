package faang.school.notificationservice.controller.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @GetMapping("/sms/test/{message}")
    public String test(@PathVariable String message) {
        UserDto userDto = new UserDto();
        smsService.send(userDto, message);
        return "ok";
    }
}
