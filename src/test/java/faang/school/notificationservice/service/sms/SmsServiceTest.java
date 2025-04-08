package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SmsService smsService;

    private final String testPhone = "79991234567";

    @Test
    void sendNotification_Success() {
        UserDto user = new UserDto();
        user.setPhone(testPhone);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{\"id\": 123, \"cnt\": 1}");

        assertDoesNotThrow(() ->
                smsService.send(user, "Test message")
        );
    }

    @Test
    void sendNotification_InvalidResponse() {
        UserDto user = new UserDto();
        user.setPhone(testPhone);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{\"error\": \"Invalid password\"}");

        assertThrows(NotificationFailedException.class,
                () -> smsService.send(user, "Test message")
        );
    }

    @Test
    void sendNotification_NullResponse() {
        UserDto user = new UserDto();
        user.setPhone(testPhone);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(null);

        assertThrows(NotificationFailedException.class,
                () -> smsService.send(user, "Test message")
        );
    }
}