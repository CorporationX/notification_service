package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.notification.telegram.TelegramMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramMessageSenderTest {

    @InjectMocks
    private TelegramMessageSender telegramMessageSender;

    @Mock
    private TelegramClient telegramClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_shouldSendMessage() throws TelegramApiException {
        SendMessage message = new SendMessage("123456789", "Test message");

        telegramMessageSender.sendMessage(message.getChatId(), message.getText());

        verify(telegramClient, times(1)).execute(message);
    }

    @Test
    void sendMessage_shouldHandleTelegramApiException() throws TelegramApiException {
        SendMessage message = new SendMessage("123456789", "Test message");

        doThrow(TelegramApiException.class).when(telegramClient).execute(message);

        TelegramApiException exception = assertThrows(TelegramApiException.class,
                () -> telegramMessageSender.sendMessage(message.getChatId(), message.getText()));

        verify(telegramClient, times(1)).execute(message);

        assertEquals(TelegramApiException.class, exception.getClass());
    }
}