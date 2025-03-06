package faang.school.notificationservice.controller;

import faang.school.notificationservice.service.impl.TelegramServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelegramNotificationControllerTest {

    @Mock
    private TelegramServiceImpl telegramService;

    @InjectMocks
    private TelegramNotificationController telegramNotificationController;

    @Test
    void testSendNotificationSuccess() {
        long userId = 12345L;
        String message = "Test notification from controller";

        when(telegramService.send(userId, message)).thenReturn(ResponseEntity.ok("Successfully sent"));

        ResponseEntity<String> response = telegramNotificationController.sendNotification(userId, message);

        verify(telegramService).send(userId, message);
        assertEquals("Successfully sent", response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }
}