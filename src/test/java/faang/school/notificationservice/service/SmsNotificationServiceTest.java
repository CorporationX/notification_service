package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsNotificationServiceHandler;
import faang.school.notificationservice.test_data.TestDataUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {
    @Mock
    private HttpClient client;
    @Mock
    private HttpRequest request;
    @Mock
    private SmsNotificationServiceHandler smsNotificationServiceHandler;
    @InjectMocks
    private SmsNotificationService smsNotificationService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        TestDataUser testDataUser = new TestDataUser();
        userDto = testDataUser.getUserDto();
        userDto.setPreference(UserDto.PreferredContact.SMS);
    }

    @Test
    public void testSend_Success() {
        String message = "TestMessage";

        when(smsNotificationServiceHandler.getHttpClient()).thenReturn(client);
        when(smsNotificationServiceHandler.getHttpRequest(userDto, message)).thenReturn(request);

        smsNotificationService.send(userDto, message);

        verify(smsNotificationServiceHandler, atLeastOnce()).getHttpClient();
        verify(smsNotificationServiceHandler, atLeastOnce()).getHttpRequest(userDto, message);
        verify(smsNotificationServiceHandler, atLeastOnce()).retryableSend(client, request);
    }

    @Test
    public void testGetPreferredContact_Success() {
        assertEquals(smsNotificationService.getPreferredContact(), userDto.getPreference());
    }
}
