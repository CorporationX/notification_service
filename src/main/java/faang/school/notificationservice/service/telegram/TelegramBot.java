package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.exception.TelegramException;
import faang.school.notificationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot  {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();

            String login = update.getMessage().getFrom().getUserName();
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            message.setChatId(chatId);
            message.setText(text);

            userRepository.updateUserTelegramId(login, chatId);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new TelegramException(ExceptionMessage.TELEGRAM_EXCEPTION, e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
