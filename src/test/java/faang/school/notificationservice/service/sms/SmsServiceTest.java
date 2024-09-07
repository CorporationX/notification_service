package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.validator.sms.SmsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsValidator smsValidator;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse smsSubmissionResponse;

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .phone("1234567890")
                .build();
        ReflectionTestUtils.setField(smsService, "brand", "MyBrand");
    }

    @Test
    void testSend_Successful() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(smsSubmissionResponse);

        smsService.send(userDto, "Test message");

        verify(vonageClient).getSmsClient();
        verify(smsValidator).validateSmsClient(smsClient);
        verify(smsValidator).validateTextMessage(any(TextMessage.class));
        verify(smsClient).submitMessage(any(TextMessage.class));
        verify(smsValidator).validateSmsResponse(eq(userDto.getPhone()), eq(smsSubmissionResponse));
    }
}
