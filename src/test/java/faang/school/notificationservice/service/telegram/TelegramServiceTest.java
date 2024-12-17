package faang.school.notificationservice.service.telegram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.User;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {
    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private TelegramBotService telegramBotService;

    @Test
    public void  testsend(){
        User user = new User();
        user.setId(1L);
        String message = "Hello";
        telegramBotService.sendMessage(user.getId(), message);
        verify(telegramBotService).sendMessage(1L, message);
    }
}