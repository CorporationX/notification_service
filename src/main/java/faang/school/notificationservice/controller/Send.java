package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/send")
@RequiredArgsConstructor
public class Send {
    private final EmailService emailService;

    @PostMapping("/a")
    public void send() {
        UserDto userDto = new UserDto();
        userDto.setEmail("pl4stmass@yandex.ru");
        emailService.send(userDto, "asfafasf");
    }
}