package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTelegramService implements NotificationService {
    private final TelegramBotService telegramBotService;
    private final TelegramService telegramService;

    @Override
    @Async("telegramBotExecutor")
    public void send(UserDto user, String message) {
        Long chatId;
        CompletableFuture<Long> completableChatId = telegramService.findChatIdByUserId(user.getId());

        try {
            chatId = completableChatId.get();
            if (chatId == null) {
                log.warn("User {} does not have a Telegram chat ID.", user.getId());
                return;
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText(message);

            try {
                telegramBotService.execute(sendMessage);
                log.info("Message sent to user {} via Telegram", user.getId());
            } catch (TelegramApiException e) {
                log.error("Failed to send Telegram message to user {}: {}", user.getId(), e.getMessage());
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
