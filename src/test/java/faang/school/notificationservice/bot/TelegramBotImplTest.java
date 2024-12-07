package faang.school.notificationservice.bot;

import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramBotImplTest {

    private TelegramBotImpl telegramBot;
    private Update update;
    private Message message;
    private Chat chat;

    @BeforeEach
    public void setUp() {
        TelegramBotConfig botConfig = mock(TelegramBotConfig.class);
        when(botConfig.getToken()).thenReturn("dummy_token");
        when(botConfig.getUsername()).thenReturn("dummy_bot");
        telegramBot = spy(new TelegramBotImpl(botConfig));
        update = new Update();
        message = new Message();
        chat = new Chat();
    }

    @Test
    @DisplayName("Test getBotUsername")
    void testGetBotUsername() {
        assertEquals("dummy_bot", telegramBot.getBotUsername());
    }

    @Test
    @DisplayName("Test onUpdateReceived")
    void testOnUpdateReceived_WithTextMessage() throws TelegramApiException {
        initializeUpdate();

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(1)).execute(argumentCaptor.capture());

        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("123456789", sentMessage.getChatId());
        assertEquals("This bot send only notifications from CorpX.", sentMessage.getText());
    }

    @Test
    @DisplayName("Test onUpdateReceived with no text message")
    void testOnUpdateReceived_NoTextMessage() throws TelegramApiException {
        initializeUpdate();
        message.setText(null);

        telegramBot.onUpdateReceived(update);

        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Test onUpdateReceived with TelegramApiException")
    void testOnUpdateReceived_ExceptionHandling() throws TelegramApiException {
        initializeUpdate();

        doThrow(new TelegramApiException("API Error")).when(telegramBot).execute(any(SendMessage.class));

        telegramBot.onUpdateReceived(update);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    private void initializeUpdate() {
        message.setText("Hello Bot!");
        chat.setId(123456789L);
        message.setChat(chat);
        update.setMessage(message);
    }
}