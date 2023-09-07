package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotConfiguration;
import faang.school.notificationservice.service.TelegramProfilesService;
import faang.school.notificationservice.service.notification.telegram.command.CommandHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    private TelegramProfilesService telegramProfilesService;
    @Mock
    private TelegramBotConfiguration telegramBotConfiguration;
    @Mock
    private CommandHistory commandHistory;
    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    void setUp() {
        telegramBotConfiguration = new TelegramBotConfiguration();
        telegramBotConfiguration.setToken("token");
        telegramBot = new TelegramBot(telegramBotConfiguration, telegramProfilesService, commandHistory);
    }

    @Test
    void onUpdateReceived() throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("start");

        Update update = new Update();
        Message message = new Message();
        message.setText("/start");
        message.setChat(new Chat(1L, "public"));
        User from = new User(1L, "username", true);
        from.setUserName("username");
        message.setFrom(from);
        update.setMessage(message);

        Mockito.when(commandHistory.execute("/start", 1L, "username")).thenReturn(sendMessage);

        telegramBot.onUpdateReceived(update);

        Mockito.verify(telegramBot, Mockito.times(1)).executeMessage(sendMessage);
    }

    @Test
    void sendMessage() {
    }

}