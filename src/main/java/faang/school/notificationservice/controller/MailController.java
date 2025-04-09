package faang.school.notificationservice.controller;

import faang.school.notificationservice.service.MailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> send(
            @RequestParam @Email String to,
            @RequestParam @Size(min = 1, max = 100) String subject,
            @RequestParam @Size(min = 1, max = 5000) String text) {
        mailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Email sent successfully");
    }
}
