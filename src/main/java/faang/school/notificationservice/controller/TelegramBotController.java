package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramBotService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/telegram-bot")
public class TelegramBotController {
    private final TelegramBotService telegramBotService;

    @PostMapping("/notify")
    public void send(@RequestBody @NotNull UserDto user) {
        telegramBotService.send(user, "Привет! Это тестовое уведомление 🚀");
    }
}
