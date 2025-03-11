package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramService;

    @PostMapping("/telegram/notification")
    public void send(@RequestBody UserDto userDto) {
        telegramService.send(userDto, "Вам пришло тестовое уведомление!");
    }
}
