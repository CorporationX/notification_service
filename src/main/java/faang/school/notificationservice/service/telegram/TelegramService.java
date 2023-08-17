package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private TelegramBot telegramBot;

    @Override
    public void send(String msg) {

    }
}
