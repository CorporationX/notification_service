package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "from", "TestSender");
    }

    @Test
    void sendSmsSuccessTest() {
        UserDto user = UserDto.builder().id(1L).phone("1234567890").build();
        String message = "Test message";

        SmsSubmissionResponse response = Mockito.mock(SmsSubmissionResponse.class);
        SmsSubmissionResponseMessage responseMessage = Mockito.mock(SmsSubmissionResponseMessage.class);

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        smsService.send(user, message);

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
        verify(response, times(1)).getMessages();
    }

    @Test
    void sendSmsWithApiFailureTest() {
        UserDto user = UserDto.builder().id(1L).phone("1234567890").build();
        String message = "Test message";

        SmsSubmissionResponse response = Mockito.mock(SmsSubmissionResponse.class);
        SmsSubmissionResponseMessage responseMessage = Mockito.mock(SmsSubmissionResponseMessage.class);

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Internal error");

        smsService.send(user, message);

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
        verify(response, times(1)).getMessages();
        verify(responseMessage, times(1)).getErrorText();
    }

    @Test
    void sendSmsFailureTest() {
        UserDto user = UserDto.builder().id(1L).phone("1234567890").build();
        String message = "Test message";

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenThrow(new RuntimeException());

        assertDoesNotThrow(() -> smsService.send(user, message));

        verify(smsClient, times(1)).submitMessage(any(TextMessage.class));
    }

    @Test
    void getPreferredContactTest() {
        UserDto.PreferredContact preferredContact = smsService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.SMS, preferredContact);
    }
}