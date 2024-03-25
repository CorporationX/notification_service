package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.repository.TelegramIdRepository;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final NotificationBot notificationBot;
    private final TelegramIdRepository telegramIdRepository;

    @Override
    public void send(UserDto user, String message) {
        Long chatId = telegramIdRepository.findByUserId(user.getId()).orElseThrow(() -> new DataValidationException("Не зарегистрирован chat_id")).getChatId();
        notificationBot.sendMessage(chatId, message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}