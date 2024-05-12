package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;

    public TelegramBot(TelegramBotConfig telegramBotConfig) {
        super(telegramBotConfig.getBotToken());
        this.telegramBotConfig = telegramBotConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            SendMessage message = startCommandReceived
        }

    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    //TODO: имя бота - CorporationXEventAlertBot
    // TODO: !!!!Нужно удалить перед загрузкой на проверку токен ТГ Бота!!!!!
}
