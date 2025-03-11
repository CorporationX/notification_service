package faang.school.notificationservice.controller;

import faang.school.notificationservice.service.TelegramService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${notification-service.api-version}/telegram")
public class TelegramNotificationController {
    private final TelegramService telegramService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@NotNull(message = "Please enter the userId") @RequestParam("user_id") Long userId,
                                                   @NotEmpty(message = "Message is empty") @RequestParam("message") String message) {
        return telegramService.send(userId, message);
    }
}
