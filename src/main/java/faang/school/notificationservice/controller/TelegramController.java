package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/telegram")
public class TelegramController {
    private final TelegramService telegramService;

    @PostMapping
    public void sendMessageToTelegram(@RequestBody UserDto user, @RequestParam String text) {
        telegramService.send(user, text);
    }
}
