package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataNotificationException;
import faang.school.notificationservice.repository.TelegramRepository;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramBot telegramBot;
    private final TelegramRepository telegramRepository;

    @Override
    public void send(UserDto user, String message, String messagesHeader) {
        telegramBot.sendNotification(telegramRepository.findByPostAuthorId(user.getId())
                .orElseThrow(() -> new DataNotificationException("Пользователь с id: " + user.getId() + " не авторизовался в CorporationX")).getChatId(), message, messagesHeader);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
