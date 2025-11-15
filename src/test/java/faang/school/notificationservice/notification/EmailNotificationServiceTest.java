package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.EmailNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationServiceTest {

    private EmailNotificationService emailService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        emailService = new EmailNotificationService();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void send_ValidUser_PrintsMessage() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("test@example.com");
        String message = "Test notification";

        // Act
        emailService.send(user, message);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("test@example.com"));
        assertTrue(output.contains("Test notification"));
    }

    @Test
    void getPreferredContact_ReturnsEmail() {
        // Act
        UserDto.PreferredContact result = emailService.getPreferredContact();

        // Assert
        assertEquals(UserDto.PreferredContact.EMAIL, result);
    }

    @Test
    void send_UserWithoutEmail_StillProcesses() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail(null);

        // Act & Assert
        assertDoesNotThrow(() -> emailService.send(user, "message"));
    }
}