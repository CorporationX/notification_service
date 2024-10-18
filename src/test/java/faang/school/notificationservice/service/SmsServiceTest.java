package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Spy
    private VonageProperties vonageProperties;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @Captor
    private ArgumentCaptor<TextMessage> messageArgumentCaptor;

    private String testSender;
    private UserDto user;
    private String text;

    @BeforeEach
    void setUp() {
        testSender = "TestSender";
        vonageProperties.setFrom(testSender);
        user = UserDto.builder()
                .id(1L)
                .username("John")
                .phone("+123456789")
                .build();
        text = "Test SMS";
    }

    @Test
    void send_shouldSendSmsSuccessfully() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        smsService.send(user, text);

        verify(responseMessage).getStatus();
        verify(smsClient).submitMessage(messageArgumentCaptor.capture());

        TextMessage sentMessage = messageArgumentCaptor.getValue();
        assertEquals(testSender, sentMessage.getFrom());
        assertEquals(user.getPhone(), sentMessage.getTo());
        assertEquals(text, sentMessage.getMessageBody());
    }

    @Test
    void send_shouldThrowMessageSendingExceptionWhenSmsFails() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Some error occurred");
        when(response.getMessages()).thenReturn(List.of(responseMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        MessageSendingException exception = assertThrows(MessageSendingException.class,
                () -> smsService.send(user, text));

        assertTrue(exception.getMessage().contains("PHONE for user 1"));
        verify(responseMessage).getErrorText();
    }

    @Test
    void getPreferredContact_shouldReturnPhone() {
        UserDto.PreferredContact preferredContact = smsService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.PHONE, preferredContact);
    }
}
