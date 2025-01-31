package faang.school.notificationservice.service.notificationServiceImpl;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserForNotificationDto user, String message) {
        log.info("Sending notification to telegram for user {}", user);
        String chatId = user.contacts().stream()
                .filter(contactDto -> contactDto.getType().equals(ContactDto.ContactType.TELEGRAM))
                .findFirst()
                .map(ContactDto::getContact)
                .orElseThrow(() -> new IllegalArgumentException("User telegram contact not found"));

        telegramBot.sendTextMessage(Long.parseLong(chatId), message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
