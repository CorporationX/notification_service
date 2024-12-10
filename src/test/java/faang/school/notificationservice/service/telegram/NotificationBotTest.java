package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.exception.ServiceCallException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationBotTest {

    @Spy
    private NotificationBot notificationBot;

    @Mock
    private Update updateMock;

    @Mock
    private Message messageMock;

    @Test
    void sendMessageValidTest() throws TelegramApiException {
        Long chatId = 123456789L;
        String text = "This chat's id is 123456789";
        doReturn(null).when(notificationBot).execute(any(SendMessage.class));

        notificationBot.sendMessage(chatId, text);

        verify(notificationBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void sendMessageServiceCallExceptionTest() throws TelegramApiException {
        Long chatId = 123456789L;
        String text = "This chat's id is 123456789";
        doThrow(TelegramApiException.class).when(notificationBot).execute(any(SendMessage.class));

        assertThrows(ServiceCallException.class, () -> notificationBot.sendMessage(chatId, text));

        verify(notificationBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void onUpdateReceivedValidTest() {
        Long chatId = 123456789L;
        String text = "This chat's id is 123456789";

        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.hasText()).thenReturn(true);
        when(messageMock.getChatId()).thenReturn(chatId);
        doNothing().when(notificationBot).sendMessage(chatId, text);

        notificationBot.onUpdateReceived(updateMock);

        verify(notificationBot, times(1)).sendMessage(chatId, text);
    }

    @Test
    void onUpdateReceivedNoMessageTest() {
        when(updateMock.hasMessage()).thenReturn(false);
        notificationBot.onUpdateReceived(updateMock);
        verify(notificationBot, never()).sendMessage(any(), any());
    }

    @Test
    void onUpdateReceivedNoTextTest() {
        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.hasText()).thenReturn(false);

        notificationBot.onUpdateReceived(updateMock);

        verify(notificationBot, never()).sendMessage(any(), any());
    }
}