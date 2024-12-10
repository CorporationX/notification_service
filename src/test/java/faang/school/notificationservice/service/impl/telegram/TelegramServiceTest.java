package faang.school.notificationservice.service.impl.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.impl.telegram.NotificationBot;
import faang.school.notificationservice.service.notification.impl.telegram.TelegramService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private NotificationBot notificationBot;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    void sendTest() {
        String message = "message";
        long telegramChatId = 89153895L;
        UserDto userDto = UserDto.builder()
                .telegramChatId(telegramChatId)
                .build();

        telegramService.send(userDto, message);

        verify(notificationBot, times(1)).sendMessage(telegramChatId, message);
    }
}