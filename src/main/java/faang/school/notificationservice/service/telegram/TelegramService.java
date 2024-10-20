package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.telegram.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService implements NotificationService {
    private final TelegramBot bot;

    @Override
    public void send(UserDto user, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getTelegramChatId());
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
            log.info("Message successfully sent from user with {} id", user.getId());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
