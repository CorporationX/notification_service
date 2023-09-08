package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotConfiguration;
import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.service.TelegramProfilesService;
import faang.school.notificationservice.service.notification.telegram.command.CommandHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    private TelegramProfilesService telegramProfilesService;
    @Mock
    private TelegramBotConfiguration telegramBotConfiguration;
    @Mock
    private CommandHistory commandHistory;
    @Spy
    @InjectMocks
    private TelegramBot telegramBot;

    @Test
    void onUpdateReceived() throws TelegramApiException {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        User from = Mockito.mock(User.class);
        Long chatId = 1L;
        String text = "start";

        SendMessage expected = new SendMessage();
        expected.setChatId(chatId);
        expected.setText(text);

        Mockito.when(update.hasMessage()).thenReturn(true);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(update.getMessage().hasText()).thenReturn(true);
        Mockito.when(update.getMessage().getText()).thenReturn("/start");
        Mockito.when(update.getMessage().getChatId()).thenReturn(chatId);
        Mockito.when(update.getMessage().getFrom()).thenReturn(from);
        Mockito.when(update.getMessage().getFrom().getUserName()).thenReturn("username");
        Mockito.when(commandHistory.execute("/start", chatId, "username")).thenReturn(expected);

        telegramBot.onUpdateReceived(update);

        Mockito.verify(telegramBot, Mockito.times(1)).execute(expected);
    }

    @Test
    void testTendMessage() throws TelegramApiException {
        long userId = 1L;
        long chatId = 123L;
        String message = "test";
        Mockito.when(telegramProfilesService.findByUserId(userId)).thenReturn(TelegramProfiles.builder().chatId(chatId).build());

        telegramBot.sendMessage(userId, message);

        Mockito.verify(telegramBot, Mockito.times(1)).execute(any(SendMessage.class));
    }

}