package faang.school.notificationservice.service;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        try {
            java.lang.reflect.Field smsTitleField = SmsService.class.getDeclaredField("smsTitle");
            smsTitleField.setAccessible(true);
            smsTitleField.set(smsService, "TestApp");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    void send_Successful() {

        UserDto user = new UserDto();
        user.setPhone("+79991234567");
        String message = "Test message";

        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
        when(responseMessage.getId()).thenReturn("message-id-123");
        when(responseMessage.getTo()).thenReturn("+79991234567");
        when(responseMessage.getErrorText()).thenReturn(null);


        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(response.getMessageCount()).thenReturn(1);

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        smsService.send(user, message);

        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void send_Failure() {

        UserDto user = new UserDto();
        user.setPhone("+79991234567");
        String message = "Test message";

        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        when(responseMessage.getStatus()).thenReturn(MessageStatus.);
        when(responseMessage.getId()).thenReturn("message-id-123");
        when(responseMessage.getTo()).thenReturn("+79991234567");
        when(responseMessage.getErrorText()).thenReturn("Invalid number");

        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(response.getMessages()).thenReturn(Collections.singletonList(responseMessage));
        when(response.getMessageCount()).thenReturn(1);

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        smsService.send(user, message);

        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void send_NullPhoneNumber_ThrowsException() {
        UserDto user = new UserDto();
        user.setPhone(null);
        String message = "Test message";

        assertThrows(IllegalArgumentException.class, () -> smsService.send(user, message));
    }

    @Test
    void getPreferredContact_ReturnsSMS() {
        UserDto.PreferredContact preferredContact = smsService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.PHONE, preferredContact);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}