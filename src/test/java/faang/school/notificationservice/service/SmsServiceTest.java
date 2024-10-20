package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    private UserDto user;
    private String message;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setPhone("1234567890");
        message = "Hello, world!";
    }

    @Test
    void testSend() {
        prepareSend(MessageStatus.OK);

        assertEquals(MessageStatus.OK, response.getMessages().get(0).getStatus());
    }

    @Test
    void testSendFail() {
        prepareSend(MessageStatus.INTERNAL_ERROR);

        assertThrows(VonageClientException.class, () -> smsService.send(user, message));
    }

    private void prepareSend(MessageStatus messageStatus) {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any())).thenReturn(response);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
        when(response.getMessageCount()).thenReturn(1);
        when(responseMessage.getStatus()).thenReturn(messageStatus);
    }
}