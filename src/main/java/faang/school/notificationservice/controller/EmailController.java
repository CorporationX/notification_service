package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.EmailRequest;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.send(emailRequest.getUser(), emailRequest.getMessage());
    }
}
