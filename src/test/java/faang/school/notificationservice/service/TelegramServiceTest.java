package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private NotificationBotService notificationBotService;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    void testSendWithUserDtoSuccess() {
        UserDto user = new UserDto();
        user.setId(12345L);
        String message = "Test notification";

        telegramService.send(user, message);

        verify(notificationBotService).sendMessage("12345", "Test notification");
    }

    @Test
    void testSendWithUserIdSuccess() {
        Long userId = 12345L;
        String message = "Test notification";

        ResponseEntity<String> response = telegramService.send(userId, message);

        verify(notificationBotService).sendMessage("12345", "Test notification");
        assertEquals("Successfully sent", response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }
}