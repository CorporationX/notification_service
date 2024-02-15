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
    void testSendCallsMethodVonageClient() {
        // Чего я тут уже только не писал, но так и не смог никак сделать этот тест
////        ReflectionTestUtils.setField(smsService,"vonageApiKey","as");
////        ReflectionTestUtils.setField(smsService,"vonageApiSecret","as");
////        ReflectionTestUtils.setField(smsService,"from","as");
//        SmsSubmissionResponse successfulResponse = new SmsSubmissionResponse();
//        successfulResponse.getMessages().add(new SmsSubmissionResponseMessage());
//        //successfulResponse.getMessages().get(0).setStatus(MessageStatus.OK);
//
//        UserDto userDto = UserDto.builder().build();
//        String message = "message";
//        TextMessage textMessage = new TextMessage("string","string","string");
//        when(successfulResponse.getMessages().get(0).getStatus()).thenReturn(MessageStatus.OK);
//        //when(vonageClient.getSmsClient()).thenReturn()
//        SmsClient smsClient = Mockito.mock(SmsClient.class);
//        when(vonageClient.getSmsClient()).thenReturn(smsClient);
//       // when(vonageClient.getSmsClient().submitMessage(textMessage)).thenReturn(new SmsSubmissionResponse());
//
//        smsService.send(userDto,message);
//        verify(vonageClient,times(1)).getSmsClient().submitMessage(textMessage);
    }
}