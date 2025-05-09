package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsNotificationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SmsService smsService;

    @Mock
    private UserDto user;

    private static final String TEST_PHONE = "79991234567";
    private static final String TEST_MESSAGE = "Test message";
    private static final String SUCCESS_RESPONSE = "{\"id\": 123, \"cnt\": 1}";
    private static final String ERROR_RESPONSE = "{\"error\": \"Invalid password\"}";

    @BeforeEach
    void setUp() {
        user.setPhone(TEST_PHONE);

        ReflectionTestUtils.setField(smsService, "toNumber", TEST_PHONE);
        ReflectionTestUtils.setField(smsService, "login", "testLogin");
        ReflectionTestUtils.setField(smsService, "password", "testPassword");
        ReflectionTestUtils.setField(smsService, "smsBaseUrl", "https://smsc.ru/sys/send.php");
    }

    @Test
    void send_WhenValidRequest() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(SUCCESS_RESPONSE);

        assertDoesNotThrow(() -> smsService.send(user, TEST_MESSAGE));

        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }

    @Test
    void send_WhenErrorResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(ERROR_RESPONSE);

        assertThrows(SmsNotificationFailedException.class,
                () -> smsService.send(user, TEST_MESSAGE));
    }

    @Test
    void send_WhenNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(null);

        assertThrows(SmsNotificationFailedException.class,
                () -> smsService.send(user, TEST_MESSAGE));
    }

    @Test
    void send_When4xxError() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(HttpClientErrorException.class,
                () -> smsService.send(user, TEST_MESSAGE));
    }

    @Test
    void send_When5xxError() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpServerErrorException.class,
                () -> smsService.send(user, TEST_MESSAGE));
    }

    @Test
    void send_WhenConnectionProblem() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection failed"));

        assertThrows(ResourceAccessException.class,
                () -> smsService.send(user, TEST_MESSAGE));
    }

    @Test
    void getPreferredContact_ShouldReturnSms() {
        assertEquals(UserDto.PreferredContact.SMS, smsService.getPreferredContact());
    }

    @Test
    void send_ShouldUseCorrectUrlParameters() {
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        when(restTemplate.getForObject(urlCaptor.capture(), eq(String.class)))
                .thenReturn(SUCCESS_RESPONSE);

        smsService.send(user, TEST_MESSAGE);

        String capturedUrl = urlCaptor.getValue();
        assertThat(capturedUrl)
                .contains("login=testLogin")
                .contains("psw=testPassword")
                .contains("phones=" + TEST_PHONE)
                .contains("fmt=3");
    }
}