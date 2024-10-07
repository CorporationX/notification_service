package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.EmailRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequestDto emailRequest) {
        UserDto user = new UserDto();
        user.setEmail(emailRequest.getEmail());
        emailService.send(user, emailRequest.getText());
        return "Email sent successfully";
    }
}
