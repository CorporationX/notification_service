package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.notification.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final SmsService service;

    @PostMapping("/send")
    public UserDto send(UserDto userDto) {
        service.send(userDto, userDto.getUsername());
        return userDto;
    }
}
