package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.VonageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.vonage.client.sms.MessageStatus.INTERNAL_ERROR;
import static com.vonage.client.sms.MessageStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsNotificationServiceTest {
    private static final String SENDER = "CorporationX";
    private static final String PHONE = "+1555555";
    private static final String SMS = "sms message";

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse smsSubmissionResponse;

    @Mock
    private List<SmsSubmissionResponseMessage> messages;

    @Mock
    private SmsSubmissionResponseMessage message;

    @InjectMocks
    private SmsNotificationService smsNotificationService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(smsNotificationService, "sender", SENDER);

        userDto = new UserDto();
        userDto.setPhone(PHONE);
    }

    @Test
    void testSend_Success() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(smsSubmissionResponse);
        when(smsSubmissionResponse.getMessages()).thenReturn(messages);
        when(messages.get(0)).thenReturn(message);
        when(message.getStatus()).thenReturn(OK);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        smsNotificationService.send(userDto, SMS);

        verify(smsClient).submitMessage(captor.capture());
        assertEquals(SENDER, captor.getValue().getFrom());
        assertEquals(PHONE, captor.getValue().getTo());
        assertEquals(SMS, captor.getValue().getMessageBody());
    }

    @Test
    void testSend_VonageClientException() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenThrow(new VonageClientException());

        assertThrows(VonageException.class, () -> {
            smsNotificationService.send(userDto, SMS);
        });
    }

    @Test
    void testSend_Exception_ResponseIsNotOk() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(smsSubmissionResponse);
        when(smsSubmissionResponse.getMessages()).thenReturn(messages);
        when(messages.get(0)).thenReturn(message);
        when(message.getStatus()).thenReturn(INTERNAL_ERROR);

        assertThrows(VonageException.class, () -> {
            smsNotificationService.send(userDto, SMS);
        });
    }

    @Test
    void testGetPreferredContact_Success() {
        UserDto.PreferredContact result = smsNotificationService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.SMS, result);
    }
}
