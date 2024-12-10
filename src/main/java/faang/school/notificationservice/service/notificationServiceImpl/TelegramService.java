package faang.school.notificationservice.service.notificationServiceImpl;

import faang.school.notificationservice.bot.TelegramBot;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserForNotificationDto user, String message) {

    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
