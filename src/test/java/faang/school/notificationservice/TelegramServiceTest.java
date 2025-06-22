package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationFailedException;
import faang.school.notificationservice.service.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    private TelegramService telegramService;

    private final String TEST_TOKEN = "test_token";
    private final String TEST_BOT_NAME = "test_bot";
    private final boolean BOT_ENABLED = true;
    private final long TEST_CHAT_ID = 123456L;
    private final String TEST_MESSAGE = "Test message";

    @BeforeEach
    void setUp() {
        telegramService = spy(new TelegramService(TEST_TOKEN, TEST_BOT_NAME, BOT_ENABLED));
    }

    @Test
    void testGetPreferredContactReturnsTelegram() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }

    @Test
    void testGetBotUsernameReturnsConfiguredUsername() {
        assertEquals(TEST_BOT_NAME, telegramService.getBotUsername());
    }

    @Test
    void testSendNullUserThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> telegramService.send(null, TEST_MESSAGE));

        assertEquals("UserDto cannot be null", exception.getMessage());
    }

    @Test
    void testSendTelegramApiFailureThrowsNotificationFailedException() throws TelegramApiException {
        UserDto user = UserDto.builder().id(TEST_CHAT_ID).build();
        TelegramApiException mockException = new TelegramApiException("API error");

        doThrow(mockException).when(telegramService).execute(any(SendMessage.class));

        NotificationFailedException exception = assertThrows(NotificationFailedException.class,
                () -> telegramService.send(user, TEST_MESSAGE));

        assertEquals(String.format("Failed to send Telegram notification to user %d", TEST_CHAT_ID),
                exception.getMessage());
        assertSame(mockException, exception.getCause());
    }
}