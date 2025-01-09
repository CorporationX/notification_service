package faang.school.notificationservice.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot{

    @Value("${telegram-bot.name}")
    private String name;

    @Value("${telegram-bot.token}")
    private String token;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String responseText;
            if (text.equalsIgnoreCase("/start")) {
                responseText = "Hello";
                SendMessage message = SendMessage.builder()
                        .chatId(chatId)
                        .text(responseText)
                        .build();
                send(message);
            }
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
            log.info("Message was sent to user {} device!", message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Error while sending telegram notification to user: {}", message.getChatId(), e);
            throw new RuntimeException(String.format("Error while sending telegram notification to user: " + message.getChatId(), e));
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
