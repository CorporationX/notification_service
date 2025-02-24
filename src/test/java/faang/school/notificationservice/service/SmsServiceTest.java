package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vanage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    private static final UserDto USER = new UserDto();
    private static final String MESSAGE = "Hi! Going out for a walk?";

    @Mock
    private SmsSubmissionResponseMessage smsSubmissionResponseMessage;

    @Mock
    private SmsSubmissionResponse smsSubmissionResponse;

    @Mock
    private SmsClient smsClient;

    @Mock
    private VonageClient vonageClient;

    @Spy
    private VonageProperties vonageProperties;

    @Captor
    private ArgumentCaptor<TextMessage> textMessageArgumentCaptor;

    @InjectMocks
    private SmsService smsService;

    @BeforeAll
    static void init() {
        USER.setId(1);
        USER.setUsername("JohnDoe");
        USER.setEmail("johndoe@example.com");
        USER.setPhone("1234567890");
        USER.setPreference(UserDto.PreferredContact.SMS);
    }

    @Test
    void send_shouldThrowSmsSendingException_whenAnInternalErrorOccurred() {
        Mockito.when(vonageClient.getSmsClient()).thenReturn(smsClient);
        Mockito.when(smsClient.submitMessage(Mockito.any(TextMessage.class))).thenReturn(smsSubmissionResponse);
        Mockito.when(smsSubmissionResponse.getMessages()).thenReturn(List.of(smsSubmissionResponseMessage));
        Mockito.when(smsSubmissionResponseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);

        Assertions.assertThrows(SmsSendingException.class, () -> smsService.send(USER, MESSAGE));

        Mockito.verify(smsSubmissionResponseMessage, Mockito.times(1)).getStatus();
        Mockito.verify(smsSubmissionResponseMessage, Mockito.times(1)).getErrorText();
    }

    @Test
    void send_shouldSendSmsSuccessfully() {
        Mockito.when(vonageClient.getSmsClient()).thenReturn(smsClient);
        Mockito.when(smsClient.submitMessage(Mockito.any(TextMessage.class))).thenReturn(smsSubmissionResponse);
        Mockito.when(smsSubmissionResponse.getMessages()).thenReturn(List.of(smsSubmissionResponseMessage));
        Mockito.when(smsSubmissionResponseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(USER, MESSAGE);

        Mockito.verify(smsSubmissionResponseMessage, Mockito.times(1)).getStatus();
        Mockito.verify(smsClient).submitMessage(textMessageArgumentCaptor.capture());

        TextMessage argumentCaptorValue = textMessageArgumentCaptor.getValue();
        Assertions.assertEquals(vonageProperties.getFrom(), argumentCaptorValue.getFrom());
        Assertions.assertEquals(USER.getPhone(), argumentCaptorValue.getTo());
        Assertions.assertEquals(MESSAGE, argumentCaptorValue.getMessageBody());
    }

    @Test
    void getPreferredContact_shouldReturnSms() {
        Assertions.assertEquals(UserDto.PreferredContact.SMS, smsService.getPreferredContact());
    }
}
