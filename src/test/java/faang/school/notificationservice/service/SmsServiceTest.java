package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.config.sms.SmsServiceProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.impl.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClientMock;
    @Mock
    private SmsServiceProperties properties;
    @Mock
    private SmsClient smsClientMock;
    @InjectMocks
    private SmsService smsService;
    @Mock
    private SmsSubmissionResponse response;
    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    private UserDto userDto;
    private String message;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(123L);
        userDto.setPhone("12345678"); //"31687519767"
        message = "Test message";
    }

    @Test
    public void testSendSuccess(){
        Mockito.when(properties.getFrom()).thenReturn("from Basilisk 8");
        Mockito.when(smsClientMock.submitMessage(any())).thenReturn(response);
        Mockito.when(vonageClientMock.getSmsClient()).thenReturn(smsClientMock);
        Mockito.when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        Mockito.when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(userDto, message);
        Mockito.verify(vonageClientMock, Mockito.times(1)).getSmsClient();
    }
}
