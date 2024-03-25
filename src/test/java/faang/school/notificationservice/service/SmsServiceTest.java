package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    @Test
    public void testSend() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);

        SmsSubmissionResponse mockResponse = mock(SmsSubmissionResponse.class);
        SmsSubmissionResponseMessage mockMessage = mock(SmsSubmissionResponseMessage.class);
        when(mockMessage.getStatus()).thenReturn(MessageStatus.OK);
        when(mockResponse.getMessages()).thenReturn(Collections.singletonList(mockMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(mockResponse);

        UserDto user = new UserDto();
        user.setPhone("1234567890");
        user.setUsername("testUser");
        String message = "Test message";
        smsService.send(user, message);

        verify(smsClient).submitMessage(any(TextMessage.class));
    }
}
