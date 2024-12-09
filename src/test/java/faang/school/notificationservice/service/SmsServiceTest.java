package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsClient smsClient;

    @Test
    void testSendSuccess() {
        UserDto dto = new UserDto();
        String jsonResponse = "{\"messages\": [{\"status\": \"0\"}]}";

        response = SmsSubmissionResponse.fromJson(jsonResponse);

        Mockito.when(vonageClient.getSmsClient()).thenReturn(smsClient);
        Mockito.when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        SmsService service = new SmsService(vonageClient);

        service.send(dto, "Hello");

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
    }
}

