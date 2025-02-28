package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private NotificationBot notificationBot;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    void testSendNotificationSuccess() {
        UserDto user = new UserDto();
        user.setId(12345L);
        String message = "Test notification";

        telegramService.send(user, message);

        verify(notificationBot).sendMessage("12345", "Test notification");
    }

    @Test
    void testSendNotificationWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> telegramService.send(null, "Test message"));
        verify(notificationBot, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void testSendNotificationWithNullMessage() {
        UserDto user = new UserDto();
        user.setId(12345L);

        assertThrows(IllegalArgumentException.class, () -> telegramService.send(user, null));
        verify(notificationBot, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }
}