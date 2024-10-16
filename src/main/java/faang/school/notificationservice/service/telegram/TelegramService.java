package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
@Slf4j
@ToString
public class TelegramService implements NotificationService {
    private final TelegramBot telegramBot;
    private final TelegramContactService contactService;

    @Override
    public void send(UserDto user, String message) {
        Update update = getUpdateForTelegramBot(user.getTelegram(), message);
        telegramBot.onUpdateReceived(update);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    private Update getUpdateForTelegramBot(String username, String message) {
        Long chatId = getChatId(username);
        Chat chat = new Chat();
        chat.setId(chatId);

        Message messageToBot = new Message();
        messageToBot.setChat(chat);
        messageToBot.setText(message);

        Update update = new Update();
        update.setMessage(messageToBot);

        return update;
    }

    private Long getChatId(String username) {
        Long chatId = contactService.getChatIdForUser(username);
        if (chatId == null) {
            String alertMessage = String.format("User %s is not subscribed to the telegram bot", username);
            log.error(alertMessage);
            throw new NotFoundException(alertMessage);
        }

        return chatId;
    }
}