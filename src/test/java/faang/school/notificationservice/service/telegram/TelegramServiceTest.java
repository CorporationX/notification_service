package faang.school.notificationservice.service.telegram;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import faang.school.notificationservice.config.telegram.BotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private BotConfig botConfig;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeEach
    void setUp() {
        // Spy the service to mock only 'execute'
        telegramService = Mockito.spy(telegramService);
    }

    @Test
    void testSendMessage_success() throws Exception {
        String chatId = "123456";
        String text = "Hello from unit test!";
        SendMessage expectedMessage = new SendMessage(chatId, text);

        doReturn(null).when(telegramService).execute(expectedMessage);

        telegramService.sendMessage(chatId, text);

        verify(telegramService, times(1)).execute(expectedMessage);
    }

    @Test
    void testSendMessage_throwsException() throws Exception {
        String chatId = "123456";
        String text = "Oops!";
        SendMessage message = new SendMessage(chatId, text);

        doThrow(new TelegramApiException("Forced failure")).when(telegramService).execute(message);

        assertThrows(RuntimeException.class, () -> telegramService.sendMessage(chatId, text));
    }
}
