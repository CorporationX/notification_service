package faang.school.notificationservice;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.model.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
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

import java.util.Optional;

import static org.mockito.Mockito.*;

class TelegramBotTest {

    @Mock
    private TelegramUserRepository telegramUserRepository;

    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnUpdateReceived_GroupChat() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.isGroupChat()).thenReturn(true);

        telegramBot.onUpdateReceived(update);

        verify(telegramUserRepository, never()).findById(anyLong());
    }

    @Test
    void testOnUpdateReceived_PrivateChat_UserExists() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setTelegramUserId(123456789L);
        telegramUser.setUserName("TestUser");

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.isGroupChat()).thenReturn(false);
        when(message.getFrom()).thenReturn(mock(org.telegram.telegrambots.meta.api.objects.User.class));
        when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.of(telegramUser));

        telegramBot.onUpdateReceived(update);

        verify(telegramUserRepository, times(1)).findById(anyLong());
    }

    @Test
    void testOnUpdateReceived_PrivateChat_UserNotFound() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User messageFrom = mock(User.class);
        messageFrom.setId(123L);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.isGroupChat()).thenReturn(false);
        when(message.getFrom()).thenReturn(messageFrom);
        when(telegramUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        telegramBot.onUpdateReceived(update);

        verify(telegramUserRepository, times(1)).findById(anyLong());
        verify(telegramUserRepository, times(1)).save(any(TelegramUser.class));
    }
}
