package faang.school.notificationservice;

import faang.school.notificationservice.config.bot.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;

@Slf4j
public class NotificationTelegramBot extends TelegramLongPollingBot {
    private BotConfig config;

    public NotificationTelegramBot(String botToken, BotConfig config) {
        super(botToken);
        this.config = config;
    }

    @Override
    public String getBotUsername() { return config.getBotName(); }
    @Override
    public String getBotToken() { return config.getToken(); }
    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();

            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                default: log.info("Unexpected message");
            }
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! I'm a Telegram bot.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

//    @Override
//    public void clearWebhook() throws TelegramApiRequestException {}


}
