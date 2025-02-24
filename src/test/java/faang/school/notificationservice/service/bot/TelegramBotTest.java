package faang.school.notificationservice.service.bot;

import faang.school.notificationservice.config.telegram.BotProperties;
import faang.school.notificationservice.exception.IntegrationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {

    private static final long CHAT_ID = 12345678L;
    private static final String MESSAGE_TEXT = "Test message";

    @Mock
    private BotProperties botProperties;

    @Mock
    private TelegramClient telegramClient;

    @InjectMocks
    private TelegramBot telegramBot;

    @Test
    void testSendMessageWhenMessageIsValid() throws TelegramApiException {
        SendMessage expectedMessage = SendMessage.builder()
                .chatId(CHAT_ID)
                .text(MESSAGE_TEXT)
                .build();

        telegramBot.sendMessage(CHAT_ID, MESSAGE_TEXT);

        verify(telegramClient, times(1)).execute(expectedMessage);
    }

    @Test
    void testSendMessageShouldThrowIntegrationException() throws TelegramApiException {
        doThrow(new TelegramApiException("API error")).when(telegramClient).execute(any(SendMessage.class));

        IntegrationException thrown = assertThrows(IntegrationException.class,
                () -> telegramBot.sendMessage(CHAT_ID, MESSAGE_TEXT));

        assertEquals("Отправка уведомления через телеграм для chat id " + CHAT_ID +
                " завершилась с ошибкой", thrown.getMessage());
    }

    @Test
    void testGetBotToken_ShouldReturnTokenFromBotProperties() {
        String expectedToken = "test-token";
        when(botProperties.getToken()).thenReturn(expectedToken);

        String actualToken = telegramBot.getBotToken();

        assertEquals(expectedToken, actualToken);
    }
}