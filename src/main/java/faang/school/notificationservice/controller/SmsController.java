package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService service;

    @PostMapping("/sms")
    public void sendSms() {
        UserDto userDto = new UserDto();
        userDto.setPhone("375259212015");
        service.send(userDto, "Test!");
    }
}
