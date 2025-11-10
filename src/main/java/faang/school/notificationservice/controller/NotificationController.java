package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.SendSmsRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.NotificationMapper;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotificationController {
    private final SmsServiceImpl smsService;
    private final NotificationMapper mapper;

    @PostMapping("/sms")
    public void sendSms(@RequestBody SendSmsRequestDto req) {
        UserDto user = mapper.toUserDto(req);
        smsService.send(user, req.message());
    }
}
