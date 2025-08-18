package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final CorporationXNotificationBot notificationBot;

    @Override
    public void send(UserNotificationDto user, String message) {
        Long userId = user.getChatId();
        notificationBot.sendBroadcast(userId, message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
