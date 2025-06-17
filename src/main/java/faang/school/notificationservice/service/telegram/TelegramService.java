package faang.school.notificationservice.service.telegram;

import org.springframework.stereotype.Service;

import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.telegram.NotificationTelegramBot;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final NotificationTelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        ContactDto contact = user.getContacts().stream()
            .filter(cont -> cont.getType().equals(UserDto.PreferredContact.TELEGRAM))
            .findFirst()
            .orElseThrow(() -> {
                log.error("The user {} is not registered with the telegram bot.", user.getId());
                return new RuntimeException(
                    String.format( "The user %d is not registered with the telegram bot.", user.getId()));
            });
        telegramBot.send(contact.getContact(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
