package faang.school.notificationservice.controller.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Validated
public class EmailController {
    public final EmailServiceImpl emailService;

    @PostMapping("/email")
    public void getEmail(@Validated UserDto user, String text) {
        emailService.send(user, text);
    }
}
