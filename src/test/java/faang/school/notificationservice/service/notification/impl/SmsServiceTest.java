package faang.school.notificationservice.service.notification.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private SmsSubmissionResponseMessage smsSubmissionResponseMessage;
    private SmsClient smsClient;

    UserDto userDto = UserDto.builder()
            .preference(UserDto.PreferredContact.SMS)
            .phone("1234567890")
            .build();

    @BeforeEach
    void setUp() {
        smsClient = mock(SmsClient.class);
        SmsSubmissionResponse smsSubmissionResponse = mock(SmsSubmissionResponse.class);
        smsSubmissionResponseMessage =
                mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient())
                .thenReturn(smsClient);
        when(smsClient.submitMessage(any(Message.class)))
                .thenReturn(smsSubmissionResponse);
        when(smsSubmissionResponse.getMessages())
                .thenReturn(List.of(smsSubmissionResponseMessage));
    }

    @Test
    void testSendSuccessCase() {
        when(smsSubmissionResponseMessage.getStatus())
                .thenReturn(com.vonage.client.sms.MessageStatus.OK);
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
}
