package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.handler.SmsSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        user.setPhone("+79991234567");
        message = "Test message";

        ReflectionTestUtils.setField(smsService, "smsTitle", "TestTitle");
    }

    @Test
    void testSendSuccessful() throws Exception {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        CompletableFuture<Void> future = smsService.send(user, message);
        future.get(3, TimeUnit.SECONDS);
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendPhoneNumberNullThrowsSmsSendingException() {
        user.setPhone(null);

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("User phone number cannot be null or empty", smsException.getMessage());
        verify(smsClient, never()).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendPhoneNumberEmptyThrowsSmsSendingException() {
        user.setPhone("");

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("User phone number cannot be null or empty", smsException.getMessage());
        verify(smsClient, never()).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendPhoneNumberInvalidFormatThrowsSmsSendingException() {
        user.setPhone("12345");

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("User phone number has incorrect format: 12345", smsException.getMessage());
        verify(smsClient, never()).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendVonageClientThrowsClientException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        VonageClientException vonageException = new VonageClientException("Vonage API error");
        when(smsClient.submitMessage(any(TextMessage.class))).thenThrow(vonageException);

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("Failed to send SMS", smsException.getMessage());
        assertEquals(vonageException, smsException.getCause());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendMessageStatusNotOkThrowsSmsSendingException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Status is not OK");

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("Internal error: Status is not OK", smsException.getMessage());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void testSendEmptyMessagesInResponseThrowsSmsSendingException() {
        when(smsVonageClient.getSmsClient()).thenReturn(smsClient);

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.emptyList());
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        CompletableFuture<Void> future = smsService.send(user, message);
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                future.get(3, TimeUnit.SECONDS));
        assertInstanceOf(SmsSendingException.class, executionException.getCause());
        SmsSendingException smsException = (SmsSendingException) executionException.getCause();
        assertEquals("Internal error: No messages in response", smsException.getMessage());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }
}