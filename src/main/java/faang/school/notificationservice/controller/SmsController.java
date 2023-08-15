package faang.school.notificationservice.controller;

import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @GetMapping("/sendSMS")
    public void sendSms() {
        smsService.send();
    }
}
