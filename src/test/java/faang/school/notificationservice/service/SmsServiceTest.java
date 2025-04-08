package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private VonageClient smsVonageClient;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    private UserDto user;
    private String message;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setPhone("71234567890");
        message = "Test message";

        ReflectionTestUtils.setField(smsService, "smsTitle", "TestTitle");
    }

    @Test
    void testSendSuccessful() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        smsService.send(user, message);

        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendPhoneNumberNullThrowsIllegalArgumentException() {
        user.setPhone(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                smsService.send(user, message));
        assertEquals("User phone number cannot be null or empty", exception.getMessage());
        verify(smsClient, never()).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendPhoneNumberEmptyThrowsIllegalArgumentException() {
        user.setPhone("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            smsService.send(user, message));
        assertEquals("User phone number cannot be null or empty", exception.getMessage());
        verify(smsClient, never()).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendVonageClientThrowsClientException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        VonageClientException vonageException = new VonageClientException("Vonage API error");
        when(smsClient.submitMessage(any(TextMessage.class))).thenThrow(vonageException);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            smsService.send(user, message));
        assertEquals("Failed to send SMS", exception.getMessage());
        assertEquals(vonageException, exception.getCause());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendMessageStatusNotOkThrowsRuntimeException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);
        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Status is not OK");

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            smsService.send(user, message));

        assertEquals("Internal error: Status is not OK", exception.getMessage());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendEmptyMessagesInResponseThrowsRuntimeException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);
        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.emptyList());
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            smsService.send(user, message));

        assertEquals("Internal error: No messages in response", exception.getMessage());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }
}