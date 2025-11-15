package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TelegramNotificationServiceTest {

    private TelegramNotificationService telegramService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        telegramService = new TelegramNotificationService();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void send_ValidUser_PrintsMessage() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("+1234567890");
        String message = "Test Telegram message";

        // Act
        telegramService.send(user, message);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("+1234567890"));
        assertTrue(output.contains("Test Telegram message"));
        assertTrue(output.contains("Telegram"));
    }

    @Test
    void getPreferredContact_ReturnsTelegram() {
        // Act
        UserDto.PreferredContact result = telegramService.getPreferredContact();

        // Assert
        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }

    @Test
    void send_UserWithoutPhone_StillProcesses() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone(null);

        // Act & Assert
        assertDoesNotThrow(() -> telegramService.send(user, "message"));
    }

    @Test
    void send_LongMessage_PrintsSuccessfully() {
        // Arrange
        UserDto user = new UserDto();
        user.setPhone("+1234567890");
        String longMessage = "This is a very long message that contains many characters " +
                "and should be sent via Telegram successfully";

        // Act
        telegramService.send(user, longMessage);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(longMessage));
    }
}