package faang.school.notificationservice.notification.telegram;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class TelegramMessageSender {

    private TelegramClient telegramClient;

    @Value("${application.telegram.telegram-token}")
    private String botToken;

    @PostConstruct
    public void initTelegramClient() {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Async("fixedThreadPool")
    public void sendMessage(String chatId, String text) throws TelegramApiException {
        log.info("Received a message to send to telegram with chatId: {}", chatId);

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
            log.info("Message was successfully sent to telegram with chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("While sending occurred an error", e);
            throw new TelegramApiException();
        }
    }
}
