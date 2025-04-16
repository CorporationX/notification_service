package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of SmsServiceTest")
public class SmsServiceTest {

    private static final String MESSAGE = "Test message";

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @Mock
    private SmsSubmissionResponse response;

    @Captor
    private ArgumentCaptor<TextMessage> captor;

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;
    private TextMessage textMessage;

    @BeforeEach
    public void setUp() {
        setUpUserDto();
        setUpTextMessage();
    }

    @Test
    @DisplayName("send - MessageStatus not equal OK")
    public void testSendWithMessageStatusNotOk() {
        setUpVonageClientMocks();
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INVALID_MESSAGE);

        assertThrows(SmsSendingException.class, () -> smsService.send(userDto, MESSAGE));
    }

    @Test
    @DisplayName("send - success")
    public void testSendSuccess() {
        setUpVonageClientMocks();
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(userDto, MESSAGE);

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
        verify(smsClient, times(1)).submitMessage(captor.capture());
        TextMessage resultTextMessage = captor.getValue();
        assertNotNull(resultTextMessage);
        assertEquals(textMessage.getMessageBody(), resultTextMessage.getMessageBody());
    }

    @Test
    @DisplayName("getPreferredContact - success")
    public void testGetPreferredContactSuccess() {
        UserDto.PreferredContact actualPreferredContact = smsService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.PHONE, actualPreferredContact);
    }

    private void setUpUserDto() {
        userDto = UserDto.builder()
                .id(1L)
                .username("User")
                .email("example@gmail.com")
                .phone("79991234567")
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }

    private void setUpTextMessage() {
        textMessage = new TextMessage("CorporationX", userDto.getPhone(), MESSAGE);
    }

    private void setUpVonageClientMocks() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
    }
}
