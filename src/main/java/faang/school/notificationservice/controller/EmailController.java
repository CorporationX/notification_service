package faang.school.notificationservice.controller;

import faang.school.notificationservice.sender.EmailServiceNext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailServiceNext emailService;

    @PostMapping
    public void sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendMail(to, subject, text);
    }
}