package faang.school.notificationservice.service.telegram;

import org.springframework.stereotype.Service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.telegram.BotKd001;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final BotKd001 botKd001;

    @Override
    public void send(UserDto user, String message) {
        botKd001.send(user, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
