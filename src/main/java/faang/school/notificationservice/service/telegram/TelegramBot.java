package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Реализация Telegram бота на основе long-polling механизма.
 * Отвечает за взаимодействие с Telegram API: получение сообщений и отправку уведомлений.
 *
 * <p>Наследует {@link TelegramLongPollingBot}, используя конфигурацию из {@link TelegramConfig}.</p>
 *
 * @see TelegramLongPollingBot
 * @see TelegramConfig
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramConfig telegramConfig;

    public TelegramBot(TelegramConfig telegramConfig) {
        super(telegramConfig.getBotToken());
        this.telegramConfig = telegramConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("I got your message, but I can't reply properly.");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Message no send. Error: {}",e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }

    /**
     * Отправляет текстовое сообщение в указанный чат.
     *
     * @param chatId ID чата в Telegram
     * @param text   текст сообщения
     * @throws TelegramApiException если возникла ошибка при отправке
     */
    public void sendMessage(long chatId, String text) throws TelegramApiException {
        log.info("Sending text to bot: {}", text);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
        log.info("Message sent to bot: {}", text);
    }
}