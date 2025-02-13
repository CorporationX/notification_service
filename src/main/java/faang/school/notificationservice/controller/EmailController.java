package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.EmailRequest;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody EmailRequest emailRequest) {
        emailService.send(emailRequest.userDto(), emailRequest.message());
        return ResponseEntity.ok(
                "Email успешно отправлен пользователю: " + emailRequest.userDto().getEmail()
        );
    }
}
