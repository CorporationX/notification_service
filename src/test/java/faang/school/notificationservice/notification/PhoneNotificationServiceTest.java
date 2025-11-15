package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.PhoneNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNotificationServiceTest {

    private PhoneNotificationService phoneService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        phoneService = new PhoneNotificationService();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void send_ValidUser_PrintsMessage() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("+1234567890");
        String message = "Test SMS";

        // Act
        phoneService.send(user, message);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("+1234567890"));
        assertTrue(output.contains("Test SMS"));
    }

    @Test
    void getPreferredContact_ReturnsPhone() {
        // Act
        UserDto.PreferredContact result = phoneService.getPreferredContact();

        // Assert
        assertEquals(UserDto.PreferredContact.PHONE, result);
    }

    @Test
    void send_DifferentPhoneNumbers_PrintsCorrectly() {
        // Arrange
        UserDto user1 = new UserDto();
        user1.setPhone("+1111111111");

        UserDto user2 = new UserDto();
        user2.setPhone("+2222222222");

        // Act
        phoneService.send(user1, "message1");
        phoneService.send(user2, "message2");

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("+1111111111"));
        assertTrue(output.contains("+2222222222"));
    }
}