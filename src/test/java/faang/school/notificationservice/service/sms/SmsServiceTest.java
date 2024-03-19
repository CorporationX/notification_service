package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private VonageClient vonageClient;
    @InjectMocks
    private SmsService smsService;

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = UserDto.PreferredContact.SMS;
        assertEquals(preferredContact, smsService.getPreferredContact());
    }

    @Test
    void testSendCallsMethodVonageClient()  {
        String vonageApiKey = "as";
        String vonageApiSecret = "as";
        String from = "as";
        ReflectionTestUtils.setField(smsService, "vonageApiKey", vonageApiKey);
        ReflectionTestUtils.setField(smsService, "vonageApiSecret", vonageApiSecret);
        ReflectionTestUtils.setField(smsService, "from", from);

        SmsClient smsClientMock = mock(SmsClient.class);
        SmsSubmissionResponse successfulResponse = mock(SmsSubmissionResponse.class);
        SmsSubmissionResponseMessage smsSubmissionResponseMessage = new SmsSubmissionResponseMessage();
        when(successfulResponse.getMessages()).thenReturn(List.of(smsSubmissionResponseMessage));
        when(smsClientMock.submitMessage(any(TextMessage.class))).thenReturn(successfulResponse);
        when(vonageClient.getSmsClient()).thenReturn(smsClientMock);

        UserDto userDto = UserDto.builder().phone("123456789").build();
        String message = "Test message";

        smsService.send(userDto, message);

        verify(smsClientMock).submitMessage(argThat(textMessage ->
                textMessage.getTo().equals(userDto.getPhone()) &&
                        textMessage.getFrom().equals(from)
        ));
    }
}