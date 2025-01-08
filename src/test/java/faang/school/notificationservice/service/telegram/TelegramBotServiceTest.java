package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.notification.TelegramConfig;
import faang.school.notificationservice.telegram.components.Buttons;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class TelegramBotServiceTest {

    @Mock
    private TelegramConfig config;

    @Mock
    private TelegramService telegramService;

    @Mock
    private ThreadPoolTaskExecutor telegramBotExecutor;

    @InjectMocks
    private TelegramBotService telegramBotService;

    @Test
    public void testGetBotUsername() {
        when(config.getBotName()).thenReturn("TestBot");

        assertEquals("TestBot", telegramBotService.getBotUsername());

        verify(config, times(1)).getBotName();
    }

    @Test
    public void testGetBotToken() {
        when(config.getToken()).thenReturn("TestBotToken");

        assertEquals("TestBotToken", telegramBotService.getBotToken());

        verify(config, times(2)).getToken();
    }

//    @Test
//    void testBotAnswerUtils_StartCommand() {
//        String receivedMessage = "/start";
//        long chatId = 123456L;
//        String userName = "TestUser";
//
//        doNothing().when(telegramBotService).startBot(chatId, userName);
//
//        telegramBotService.botAnswerUtils(receivedMessage, chatId, userName);
//
//        verify(telegramBotService).startBot(chatId, userName);
//        verifyNoMoreInteractions(telegramBotService);
//    }

    @Test
    public void testStartBot_Success() throws TelegramApiException {
        // Мокаем TelegramBotService
        TelegramBotService bot = Mockito.mock(TelegramBotService.class);

        long chatId = 12345L;
        String userName = "testUser";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hi, " + userName + "! I'm Corporation X bot.");
        sendMessage.setReplyMarkup(Buttons.secondInlineMarkup());

        // Мокаем вызов метода execute
        doAnswer(invocation -> null).when(bot).execute(eq(sendMessage));

        // Вызываем метод startBot
        telegramBotService.startBot(chatId, userName);

        // Проверяем, что execute был вызван с правильным сообщением
        verify(bot).execute(eq(sendMessage));
    }
}

