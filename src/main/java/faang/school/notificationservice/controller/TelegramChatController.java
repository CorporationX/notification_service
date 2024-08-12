package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.TelegramChatDto;
import faang.school.notificationservice.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification/")
public class TelegramChatController {

    private final TelegramChatService telegramChatService;

    @PostMapping("")
    public void createChatId(TelegramChatDto telegramChatDto){
        telegramChatService.createChatId(telegramChatDto);
    }

}
