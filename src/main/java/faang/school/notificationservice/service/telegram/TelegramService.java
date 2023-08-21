package faang.school.notificationservice.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            System.out.println(chat_id);

            SendMessage message = new SendMessage(); // Create a message object object
                    message.setChatId(chat_id);
                    message.setText(message_text);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @PostConstruct
    public void send(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(441979941L);
        sendMessage.setText("Hi i am start");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "CorpX_bot";
    }

    @Override
    public String getBotToken() {
        return "6465525589:AAG0RzvhuGF398lj793npi-le8arqZcTxJQ";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
