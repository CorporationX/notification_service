package faang.school.notificationservice;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.feign.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TelegramBotTest {

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnUpdateReceived_WithMessage_FromGroupChat() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User messageFrom = mock(User.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.isGroupChat()).thenReturn(true);
        when(chat.getId()).thenReturn(12345L);
        when(message.getFrom()).thenReturn(messageFrom);

        telegramBot.onUpdateReceived(update);

        verify(userServiceClient, never()).updateTelegramUserId(anyString(), anyString());
    }

    @Test
    public void testOnUpdateReceived_WithMessage_FromPrivateChat() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User user = mock(User.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.isGroupChat()).thenReturn(false);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(12345L);
        when(user.getUserName()).thenReturn("testUser");

        telegramBot.onUpdateReceived(update);

        verify(userServiceClient, times(1)).updateTelegramUserId("testUser", "12345");
    }
}
