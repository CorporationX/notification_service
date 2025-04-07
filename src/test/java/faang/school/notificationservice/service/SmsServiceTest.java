package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @Mock
    private VonageClient vonageClient;

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;
    private String message;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.PHONE)
                .phone("1234567")
                .build();

        message = "Send SMS";
    }

    @Test
    void testSendSuccess() {
        when(smsClient.submitMessage(any())).thenReturn(response);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(userDto, message);
        verify(vonageClient, times(1)).getSmsClient();
    }

    @Test
    void testSendFailure() {
        when(smsClient.submitMessage(any())).thenReturn(response);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Error sending SMS");

        SmsIntegrationException exception = assertThrows(SmsIntegrationException.class,
                () -> smsService.send(userDto, message));

        assertEquals("SMS error: Error sending SMS", exception.getMessage());
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void testSend_WithoutPhoneNumber() {
        userDto.setPhone(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> smsService.send(userDto, message));

        assertEquals("To send SMS, a phone number is a mandatory requirement", exception.getMessage());
    }

    @Test
    void testSend_WithEmptyPhoneNumber() {
        userDto.setPhone("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> smsService.send(userDto, message));

        assertEquals("To send SMS, a phone number is a mandatory requirement", exception.getMessage());
        verifyNoInteractions(vonageClient);
    }

    @Test
    void testSend_WithEmptyResponse() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any())).thenReturn(null);

        SmsIntegrationException exception = assertThrows(SmsIntegrationException.class,
                () -> smsService.send(userDto, message));

        assertEquals("Incorrect response from Vonage", exception.getMessage());
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void testSend_WithExceptionFromClient() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any())).thenThrow(new RuntimeException("Client error"));

        SmsIntegrationException exception = assertThrows(SmsIntegrationException.class,
                () -> smsService.send(userDto, message));

        assertEquals("Error sending SMS", exception.getMessage());
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void testGetPreferredContact() {
        assertEquals(UserDto.PreferredContact.PHONE, smsService.getPreferredContact());
    }
}
