package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private UserDto user;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @BeforeEach
    void setUp() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    public void successfullySendSms() {
        when(user.getPhone()).thenReturn("1234567890");
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        String message = "Test message";

        smsService.send(user, message);

        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient).submitMessage(messageCaptor.capture());
        TextMessage capturedMessage = messageCaptor.getValue();

        assertEquals("1234567890", capturedMessage.getTo());
        assertEquals(message, capturedMessage.getMessageBody());
    }

    @Test
    public void failedToSendSms() {
        when(user.getPhone()).thenReturn("1234567890");
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INVALID_MESSAGE);
        when(responseMessage.getErrorText()).thenReturn("Some error");

        String message = "Test message";

        SmsSendingException exception = assertThrows(SmsSendingException.class, () -> smsService.send(user, message));

        assertEquals("Message sending failed: Some error", exception.getMessage());
    }
}
