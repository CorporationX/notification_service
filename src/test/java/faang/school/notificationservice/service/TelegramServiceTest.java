package faang.school.notificationservice.service;

import faang.school.notificationservice.service.impl.NotificationBotService;
import faang.school.notificationservice.service.impl.TelegramServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private NotificationBotService notificationBotService;

    @InjectMocks
    private TelegramServiceImpl telegramService;

    @Test
    void testSendWithUserIdSuccess() {
        String message = "Test notification";
        telegramService.send(12345L, message);

        verify(notificationBotService).sendMessage("12345", "Test notification");
    }
}