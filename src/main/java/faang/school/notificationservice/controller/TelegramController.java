package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.SendingDto;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService service;

    @RequestMapping("/notification/telegram")
    public void sendMessage(@RequestBody SendingDto dto) {
        service.send(dto.userDto(), dto.message());
    }
}
