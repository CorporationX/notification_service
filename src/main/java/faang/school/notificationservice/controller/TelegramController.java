package faang.school.notificationservice.controller;

import faang.school.notificationservice.client.telegram.bot.CorpXTelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/telegram")
@RestController
public class TelegramController {
    private final CorpXTelegramBot telegramBot;

    @GetMapping("/connect")
    public ResponseEntity<String> connectToTelegram() {
        String startLink = telegramBot.generateStartLink();
        return ResponseEntity.ok(startLink);
    }
}
