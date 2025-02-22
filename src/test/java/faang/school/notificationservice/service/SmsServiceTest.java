package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;

    @Mock
    private VonageClient vonageClient;

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;
    private String message;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.SMS)
                .phone("1234567")
                .build();
        message = "Send SMS";
    }

    @Test
    void testSendSuccess() {
        when(smsClient.submitMessage(any())).thenReturn(response);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(userDto, message);
        verify(vonageClient, times(1)).getSmsClient();
    }

    @Test
    void testSendFailureWithoutLogCaptor() {

        when(smsClient.submitMessage(any())).thenReturn(response);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Ошибка отправки SMS");

        assertDoesNotThrow(() -> smsService.send(userDto, message));

        verify(smsClient, times(1)).submitMessage(any());
    }
}