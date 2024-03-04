package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping()
    public ResponseEntity<String> sendSms(@RequestBody UserDto userDto, @RequestParam String message) {
        smsService.send(userDto, message);
        return ResponseEntity.ok("SMS sent successfully");
    }
}
