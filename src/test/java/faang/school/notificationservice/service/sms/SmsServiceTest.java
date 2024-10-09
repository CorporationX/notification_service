package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setPhone("121212");
    }

    @Test
    @DisplayName("Check preferred contact")
    public void testPreferredContact() {
        assertEquals(UserDto.PreferredContact.SMS, smsService.getPreferredContact());
    }

    @Test
    @DisplayName("Successfully send sms")
    public void testSendSms() {
        String message = "test message";
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(smsSubmissionResponse);

        smsService.send(userDto, message);
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        verify(smsClient).submitMessage(captor.capture());
        assertEquals(userDto.getPhone(), captor.getValue().getTo());
        assertEquals(message, captor.getValue().getMessageBody());
    }
}
