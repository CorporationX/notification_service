package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;

@Service
@Slf4j
public class TelegramService implements NotificationService {
    @Autowired
    private TelegramBot bot;

    @Override
    public void send(UserDto user, String message) {
        String chatId = String.valueOf(user.getId());
        bot.sendMessage(chatId, message);
        log.info("TelegramBot received a message.");
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return TELEGRAM;
    }
}