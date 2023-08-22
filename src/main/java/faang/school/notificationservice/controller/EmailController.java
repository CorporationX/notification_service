package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.EmailSendDto;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @PostMapping()
    public void sendEmail(@RequestBody EmailSendDto emailSendDto){
        emailService.send(emailSendDto.getUserDto(), emailSendDto.getMessage());
    }
}
