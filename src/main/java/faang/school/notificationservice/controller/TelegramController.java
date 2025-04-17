package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService telegramService;

    @PostMapping("/telegramId/{message}")
    public ResponseEntity<String> notifyTelegram(@RequestBody UserDto userDto, @PathVariable String message) {

            telegramService.send(userDto, message);
            return ResponseEntity.ok("Уведомление успешно отправлено");
    }
}
