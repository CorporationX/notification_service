package faang.school.notificationservice.controller;

import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.EmailRequestDto;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/mails")
    public String sendSimpleEmail(@RequestBody EmailRequestDto emailRequestDto, @RequestHeader Long userId) {
        log.info("Сообщение в контроллере");
        return emailService.sendSimpleEmail(emailRequestDto.getTo(),
                emailRequestDto.getSubject(), emailRequestDto.getText(), userId);
    }
}
