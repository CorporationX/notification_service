package faang.school.notificationservice;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.VonageConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserEventDto;
import faang.school.notificationservice.exception.SmsSendingException;
import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @Mock
    private VonageConfig config;
    @Mock
    private VonageClient vonageClient;
    @Mock
    private SmsClient smsClient;
    @InjectMocks
    private SmsService smsService;

    @Mock
    private SmsSubmissionResponseMessage successMessage;
    @Mock
    private SmsSubmissionResponse response;

    private UserEventDto userDto;
    private String message;

    @BeforeEach
    void setup() {
        userDto = new UserEventDto();
        userDto.setPhone("1234567890");
        message = "Hello!";
        when(config.getFrom()).thenReturn("faang");
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(response.getMessages()).thenReturn(List.of(successMessage));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
    }

    @Test
    void sendTest_Success() {
        when(successMessage.getStatus()).thenReturn(MessageStatus.OK);

        assertDoesNotThrow(() -> smsService.send(userDto, message));
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void sendTest_ShouldThrowExceptionWhenSmsDoesNotSend() {
        when(successMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);

        SmsSendingException exception = assertThrows(SmsSendingException.class,
                () -> smsService.send(userDto, message));

        assertEquals("Failed to send message to 1234567890", exception.getMessage());
        verify(smsClient).submitMessage(any(TextMessage.class));
    }
}
