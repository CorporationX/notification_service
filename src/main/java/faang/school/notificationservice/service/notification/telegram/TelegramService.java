package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void sendNotification(Long id, String message) {

        telegramBot.sendMessage(id, message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
