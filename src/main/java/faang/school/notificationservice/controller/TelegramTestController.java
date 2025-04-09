package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Этот контроллер создан только для тестирования корректности работы
 * 1 запустит проект
 * 2 нати в телеграмме бота по имени MyRamilNotifierBot
 * 3 написат /start
 * 4 скопироват с консоли chatId
 * 5 в постмане в теле отрпавит json c {"telegramId" : "chatId"}
 * @see TelegramTestController и его метода
 * @see TelegramTestController#sendNotification(UserDto) - отправляет уведомление текстовое сообщением
 */

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class TelegramTestController {

    private final TelegramService telegramService;

    @PostMapping("/telegram")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody UserDto userDto) {
        telegramService.send(userDto, "Привет !!! я телеграм бот ");

        Map<String, String> response = Map.of(
                "message", String.format("User %s get telegram message", userDto.getUsername())
        );

        return ResponseEntity.ok(response);
    }
}
