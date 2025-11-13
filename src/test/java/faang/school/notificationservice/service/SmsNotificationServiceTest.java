package faang.school.notificationservice.service;

import faang.school.notificationservice.config.provider.SmsRuProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SmsNotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SmsNotificationService service;

    @Test
    void sendValidPhoneCallsRestTemplate() {
        SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
        SmsNotificationService service = new SmsNotificationService(restTemplate, properties);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("79991234567");

        service.send(user, "Test message");

        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void sendNullPhoneDoesNotCallRestTemplate() {
        SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
        SmsNotificationService service = new SmsNotificationService(restTemplate, properties);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone(null);

        service.send(user, "Test message");

        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void sendInvalidPhoneDoesNotCallRestTemplate() {
        SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
        SmsNotificationService service = new SmsNotificationService(restTemplate, properties);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("12345");

        service.send(user, "Test message");

        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void sendBlankPhoneDoesNotCallRestTemplate() {
        SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
        SmsNotificationService service = new SmsNotificationService(restTemplate, properties);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("   ");

        service.send(user, "Test message");

        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getPreferredContact_ReturnsPhone() {
        SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
        SmsNotificationService service = new SmsNotificationService(restTemplate, properties);

        assertEquals(UserDto.PreferredContact.PHONE, service.getPreferredContact());
    }
}