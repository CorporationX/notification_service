package faang.school.notificationservice.service.telegram;


import faang.school.notificationservice.config.notification.TelegramConfig;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    @Mock
    private DefaultAbsSender defaultAbsSender;

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
}

