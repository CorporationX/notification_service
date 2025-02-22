package faang.school.notificationservice.service.notification.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @Mock
    private VonageClient vonageClient;
    @InjectMocks
    private SmsService smsService;

    private SmsSubmissionResponseMessage smsSubmissionResponseMessage1;
    private SmsSubmissionResponseMessage smsSubmissionResponseMessage2;
    private SmsClient smsClient;

    UserDto userDto = UserDto.builder()
            .preference(UserDto.PreferredContact.SMS)
            .phone("1234567890")
            .build();

    @BeforeEach
    void setUp() {
        smsClient = mock(SmsClient.class);
        SmsSubmissionResponse smsSubmissionResponse = mock(SmsSubmissionResponse.class);
        smsSubmissionResponseMessage1 =
                mock(SmsSubmissionResponseMessage.class);
        smsSubmissionResponseMessage2 =
                mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient())
                .thenReturn(smsClient);
        when(smsClient.submitMessage(any(Message.class)))
                .thenReturn(smsSubmissionResponse);
        when(smsSubmissionResponse.getMessages())
                .thenReturn(List.of(smsSubmissionResponseMessage1, smsSubmissionResponseMessage2));
    }

    @Test
    void testSendSuccessCase() {
        when(smsSubmissionResponseMessage1.getStatus())
                .thenReturn(MessageStatus.OK);
        when(smsSubmissionResponseMessage2.getStatus())
                .thenReturn(MessageStatus.OK);
        String message = "Message";

        smsService.send(userDto, message);

        ArgumentCaptor<TextMessage> messageArgumentCaptor =
                ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1))
                .submitMessage(messageArgumentCaptor.capture());
        TextMessage captureMessage = messageArgumentCaptor.getValue();
        assertEquals(message, captureMessage.getMessageBody());
        assertEquals(userDto.getPhone(), captureMessage.getTo());
    }

    @Test
    void testSendWithErrors() {
        when(smsSubmissionResponseMessage1.getStatus())
                .thenReturn(MessageStatus.INVALID_CALLBACK);
        when(smsSubmissionResponseMessage1.getErrorText())
                .thenReturn("Error 1");
        when(smsSubmissionResponseMessage2.getStatus())
                .thenReturn(MessageStatus.INVALID_CALLBACK);
        when(smsSubmissionResponseMessage2.getErrorText())
                .thenReturn("Error 2");
        String message = "Message";

        assertThrows(
                ExternalServiceException.class,
                () -> smsService.send(userDto, message),
                "Failed to send SMS: Error 1,Error 2"
        );
    }
}
