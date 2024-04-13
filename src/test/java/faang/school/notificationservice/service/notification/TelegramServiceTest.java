package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    void send_ValidArgs() {
        telegramService.send(new UserDto(), "");

        verify(telegramBot, times(1)).sendMessage(anyLong(), anyString());
    }
}
