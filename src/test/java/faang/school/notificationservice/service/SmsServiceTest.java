package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SmsServiceTest {
    @InjectMocks
    private SmsService smsService;
    @Mock
    private VonageClient vonageClient;
    private UserDto user;
    private String text;
    private SmsClient smsClient;

    @BeforeEach
    void setUp() {
        user = UserDto.builder().phone("PHONE").build();
        text = "TEXT";
        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        smsClient = mock(SmsClient.class);
        SmsSubmissionResponseMessage message = mock(SmsSubmissionResponseMessage.class);

        when(response.getMessages()).thenReturn(List.of(message));
        when(smsClient.submitMessage(any())).thenReturn(response);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    void getPreferredContact_shouldReturnPhone() {
        assertEquals(UserDto.PreferredContact.PHONE, smsService.getPreferredContact());
    }

    @Test
    void send_shouldInvokeSubmitMessageMethod() {
        smsService.send(user, text);
        verify(vonageClient).getSmsClient();
        verify(smsClient).submitMessage(any());
    }
}