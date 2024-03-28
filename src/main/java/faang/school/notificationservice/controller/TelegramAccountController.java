package faang.school.notificationservice.controller;

import faang.school.notificationservice.service.telegram.TelegramAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramAccountController {
    private final TelegramAccountService telegramAccountService;

    @PostMapping("/{uuid}/confirm")
    public void confirmAccount (@PathVariable UUID uuid) {
        telegramAccountService.confirmAccount(uuid);
    }
}
