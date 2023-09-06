package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.repository.TelegramProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramProfileRepository telegramProfileRepository;
    private final TelegramBotConfig config;

    @Autowired
    public TelegramBot(TelegramProfileRepository telegramProfileRepository, TelegramBotConfig config) {
        super(config.getToken());
        this.telegramProfileRepository = telegramProfileRepository;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            if (messageText.equals("/start")) {
                startBot(chatId, userName);
            } else {
                log.info("Unexpected message");
            }
        }
    }

    private void startBot(long chatId, String userName) {
        if (telegramProfileRepository.existsByUserName(userName)) {
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! Now you are authorized.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
