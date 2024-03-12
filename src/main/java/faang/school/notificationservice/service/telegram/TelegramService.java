package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramNotificationBot telegramNotificationBot;
    private final TelegramUserRepository telegramUserRepository;


    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void send(UserDto userDto, String message) {
        TelegramUser telegramUser = telegramUserRepository.findByUserId(userDto.getId())
                .orElseThrow(() -> new RuntimeException("ID телеграмм-чата не найден"));
        telegramNotificationBot.sendNotification(telegramUser.getChatId(), message);
        log.info("Message sent successfully");
    }
}
