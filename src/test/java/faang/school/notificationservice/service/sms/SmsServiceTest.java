package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @Mock
    private VonageClient vonageClient;
    @Mock
    private SmsClient smsClient;
    @InjectMocks
    SmsService smsService;

    UserDto user;
    Long id = 1L;

    String phoneNumber = "+17777777777";
    String message;
    String from;

    TextMessage sms;

    @Mock
    SmsSubmissionResponse response;


    @BeforeEach
    void init() {
        user = UserDto.builder().build();
        user.setId(id);
        user.setPhone(phoneNumber);

        message = "You have new follower";
        from = "CorporationX";
        sms = new TextMessage(from, user.getPhone(), message);

    }

    @Test
    public void sendWithNoPhoneNumberTest() {
        user.setPhone(" ");

        assertThrows(NotificationServiceException.class, () -> smsService.send(user, message));
    }

    @Test
    public void sendSuccessfulTest() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(sms)).thenReturn(response);
        when(vonageClient.getSmsClient().submitMessage(sms)).thenReturn(response);

        SmsSubmissionResponse receivedResponse = vonageClient.getSmsClient().submitMessage(sms);
        assertEquals(response, receivedResponse);
        verify(smsClient, times(1)).submitMessage(sms);
    }

    @Test
    public void sendUnsuccessfulTest() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(sms)).thenReturn(response);
        when(vonageClient.getSmsClient().submitMessage(sms)).thenReturn(response);

        SmsSubmissionResponse receivedResponse = vonageClient.getSmsClient().submitMessage(sms);
        assertEquals(response, receivedResponse);
        verify(smsClient, times(1)).submitMessage(sms);

    }
}
