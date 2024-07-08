package faang.school.notificationservice.service.bot;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String notification) {
        log.info("Send message to Telegram Bot: {} for User: {}", notification, user.getTelegramChatId());
        Chat chat = Chat.builder()
                .id(user.getTelegramChatId())
                .type("private")
                .firstName(user.getUsername())
                .build();
        Message message = Message.builder()
                .text(notification)
                .date((int) new Date().getTime())
                .chat(chat)
                .build();
        Update update = new Update();
        update.setMessage(message);
        telegramBot.consume(update);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
