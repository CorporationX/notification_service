package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import faang.school.notificationservice.service.telegram.NotificationBot;
import faang.school.notificationservice.service.telegram.NotificationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramServiceTest {

    private NotificationBot notificationBot;
    private TelegramService telegramService;

    @BeforeEach
    void setUp() {
        notificationBot = mock(NotificationBot.class);
        telegramService = new TelegramService(notificationBot);
    }

    @Test
    void getPreferredContact_ShouldReturnTelegram() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }

    @Test
    void send_ShouldCallNotificationBotSendMessage() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(123456L);
        String message = "Test message";

        telegramService.send(user, message);

        verify(notificationBot, times(1)).sendMessage(123456L, message);
    }

    @Test
    void send_NullUser_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> telegramService.send(null, "msg"));
        assertEquals("UserDto cannot be null", exception.getMessage());
    }

    @Test
    void send_WhenTelegramApiException_ShouldThrowNotificationFailedException() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(42L);
        String message = "Hello";

        doThrow(new TelegramApiException("API error")).when(notificationBot).sendMessage(42L, message);

        NotificationFailedException exception = assertThrows(NotificationFailedException.class,
                () -> telegramService.send(user, message));

        assertTrue(exception.getMessage().contains("Failed to send Telegram notification to user 42"));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof TelegramApiException);
    }
}
