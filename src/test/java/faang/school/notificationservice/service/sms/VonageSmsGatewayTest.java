package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.exception.SmsSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VonageSmsGatewayTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse submissionResponse;

    @Mock
    private SmsSubmissionResponseMessage messageResponse;

    @InjectMocks
    private VonageSmsGateway smsGateway;

    private final String from = "TestSender";
    private final String recipientPhoneNumber = "+48123456789";
    private final String message = "Hello from Vonage!";

    @BeforeEach
    void setUp() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    @DisplayName("Should send SMS successfully when Vonage response status is OK")
    void shouldSendSmsSuccessfully() {
        when(messageResponse.getStatus()).thenReturn(MessageStatus.OK);
        when(submissionResponse.getMessages()).thenReturn(List.of(messageResponse));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(submissionResponse);

        assertDoesNotThrow(() -> smsGateway.send(from, recipientPhoneNumber, message));
    }

    @Test
    @DisplayName("Should throw SmsSendException when Vonage response status is not OK")
    void shouldThrowWhenStatusIsNotOk() {
        when(messageResponse.getStatus()).thenReturn(MessageStatus.THROTTLED);
        when(messageResponse.getErrorText()).thenReturn("Throttled");
        when(submissionResponse.getMessages()).thenReturn(List.of(messageResponse));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(submissionResponse);

        SmsSendException exception = assertThrows(SmsSendException.class,
                () -> smsGateway.send(from, recipientPhoneNumber, message));

        assertTrue(exception.getMessage().contains("Vonage SMS failed: Throttled"));
    }

    @Test
    @DisplayName("Should throw SmsSendException when response is null")
    void shouldThrowWhenResponseIsNull() {
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(null);

        SmsSendException exception = assertThrows(SmsSendException.class,
                () -> smsGateway.send(from, recipientPhoneNumber, message));

        assertEquals("Invalid Vonage SMS response", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw SmsSendException when message list is empty")
    void shouldThrowWhenMessageListIsEmpty() {
        when(submissionResponse.getMessages()).thenReturn(Collections.emptyList());
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(submissionResponse);

        SmsSendException exception = assertThrows(SmsSendException.class,
                () -> smsGateway.send(from, recipientPhoneNumber, message));

        assertEquals("Invalid Vonage SMS response", exception.getMessage());
    }

    @Test
    @DisplayName("Should propagate VonageClientException when client throws exception")
    void shouldThrowVonageClientException() {
        when(smsClient.submitMessage(any(TextMessage.class)))
                .thenThrow(new VonageClientException("Network failure"));

        assertThrows(VonageClientException.class, () -> smsGateway.send(from, recipientPhoneNumber, message));
    }
}