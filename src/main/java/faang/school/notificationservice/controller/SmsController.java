package faang.school.notificationservice.controller;

import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/send")
public class SmsController {
    private final SmsService smsService;
    private final UserContext userContext;

    @PostMapping
    private String sendSms(@RequestBody String msg) {
        return smsService.sendSms(msg, userContext.getUserId());
    }
}
