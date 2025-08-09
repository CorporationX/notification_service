package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final CorporationXNotificationBot notificationBot;

    @Override
    public void send(UserDto user, String message) {
        Long userId = user.getId();
        notificationBot.sendBroadcast(userId, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
