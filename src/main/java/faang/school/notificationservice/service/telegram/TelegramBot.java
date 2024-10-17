package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;

    public TelegramBot(TelegramBotProperties properties) {
        super(properties.getToken());
        this.properties = properties;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String stickerFileId = "CAACAgIAAxkBAAEDRcdhfhJijljkljsidfjlsd_RqO8AAGyAAJ9p0kUKvC_rEIj4PgkHwQ";
            InputFile sticker = new InputFile(stickerFileId);

            SendSticker sendSticker = new SendSticker();
            sendSticker.setChatId(chatId.toString());
            sendSticker.setSticker(sticker);

            executeAsync(sendSticker);
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }
}
