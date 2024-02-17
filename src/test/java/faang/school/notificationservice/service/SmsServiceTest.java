package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @BeforeEach
    public void setup() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    // не пойму как тест написать
    @Test
    public void sendSmsSuccessfully() {
        UserDto user = new UserDto();
        user.setUsername("testUser");
        user.setPhone("1234567890");

        SmsSubmissionResponse smsSubmissionResponse = mock(SmsSubmissionResponse.class);
        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
        when(smsSubmissionResponse.getMessages()).thenReturn(Collections.singletonList(responseMessage));

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(smsSubmissionResponse);

        smsService.send(user, "Test message");

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
    }
}
