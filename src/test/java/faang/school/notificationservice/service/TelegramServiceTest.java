package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegrambot.TelegramBot;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {
    private static UserDto user;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeAll
    static void setUp() {
        user = UserDto.builder()
                .id(123L)
                .username("testUser")
                .email("email@email")
                .phone("111222333")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    void testSendNotificationSuccessfully() {
        String message = "Test message";

        telegramService.send(user, message);

        verify(telegramBot).sendNotification(123L, message);
    }

    @Test
    void testNoSuccessSendNotification() {
        String message = "Test message";

        doThrow(new RuntimeException("Telegram error"))
                .when(telegramBot).sendNotification(anyLong(), anyString());

        assertThrows(RuntimeException.class,
                () -> telegramService.send(user, message));
    }
}