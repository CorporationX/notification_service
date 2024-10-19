package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse smsSubmissionResponse;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @InjectMocks
    private SmsService smsService;


    @Test
    public void testSendSmsSuccess() {
        UserDto user = new UserDto();
        user.setUsername("john_doe");
        user.setPhone("+1234567890");

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(Message.class))).thenReturn(smsSubmissionResponse);
        when(smsSubmissionResponse.getMessages()).thenReturn(List.of(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(user, "Test message");

        verify(smsClient).submitMessage(any(TextMessage.class));
        verify(smsSubmissionResponse).getMessages();
    }

    @Test
    public void testSendSmsFailure() {
        UserDto user = new UserDto();
        user.setUsername("john_doe");
        user.setPhone("+1234567890");

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(Message.class))).thenThrow(new RuntimeException());

        smsService.send(user, "Test message");

        verify(smsClient).submitMessage(any(TextMessage.class));
        verify(smsSubmissionResponse, times(0)).getMessages();
    }
}