package faang.school.notificationservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificatonController {
    private final TelegramService telegramService;

    @PostMapping("/telegram")
    public void sendTelegramMessage(@RequestBody UserDto userDto, @RequestParam String message) {
        telegramService.send(userDto, message);
    }
}
