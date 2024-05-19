package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequiredArgsConstructor
public class MailController {

   private final MailService mailService;

    //just to test
   @PostMapping("/mail")
    public void sendEmail(){
        mailService.send(new UserDto(), "message");
    }
}
