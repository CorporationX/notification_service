package faang.school.notificationservice.smsservicetests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.SmsProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsServiceException;
import faang.school.notificationservice.service.sms.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    private SmsService smsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        SmsProperties smsProperties = new SmsProperties();
        smsProperties.setKey("test-key");
        smsProperties.setSecret("test-secret");
        smsProperties.setFromService("Vonage APIs");

        smsService = new SmsService(smsProperties);

        // Устанавливаем приватное поле client через reflection
        Field clientField = SmsService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(smsService, vonageClient);

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    @DisplayName("Should send SMS successfully when Vonage response status is OK")
    void shouldSendSmsSuccessfully_whenResponseStatusIsOk() throws Exception {
        UserDto user = new UserDto();
        user.setPhone("+79999693105");

        SmsSubmissionResponseMessage message = new SmsSubmissionResponseMessage();
        setPrivateField(message, "status", MessageStatus.OK);

        SmsSubmissionResponse response = new SmsSubmissionResponse();
        setPrivateField(response, "messageCount", 1);
        setPrivateField(response, "messages", List.of(message));

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        assertDoesNotThrow(() -> smsService.send(user, "Test message"));
    }

    @Test
    @DisplayName("Should throw SmsServiceException when Vonage response status is not OK")
    void shouldThrowSmsServiceException_whenResponseStatusIsNotOk() throws Exception {
        UserDto user = new UserDto();
        user.setPhone("+79999693105");

        SmsSubmissionResponseMessage message = new SmsSubmissionResponseMessage();
        setPrivateField(message, "status", MessageStatus.INTERNAL_ERROR);
        setPrivateField(message, "errorText", "Some error");

        SmsSubmissionResponse response = new SmsSubmissionResponse();
        setPrivateField(response, "messageCount", 1);
        setPrivateField(response, "messages", List.of(message));

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        SmsServiceException ex = assertThrows(SmsServiceException.class,
                () -> smsService.send(user, "Test message"));

        assertTrue(ex.getMessage().contains("Some error"));
    }

    @Test
    @DisplayName("Should throw SmsServiceException when Vonage response messages list is empty")
    void shouldThrowSmsServiceException_whenResponseMessagesListIsEmpty() throws Exception {
        UserDto user = new UserDto();
        user.setPhone("+79999693105");

        SmsSubmissionResponse response = new SmsSubmissionResponse();
        setPrivateField(response, "messageCount", 0);
        setPrivateField(response, "messages", List.of());

        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        SmsServiceException ex = assertThrows(SmsServiceException.class,
                () -> smsService.send(user, "Test message"));

        assertTrue(ex.getMessage().contains("no messages"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when phone number is invalid")
    void shouldThrowIllegalArgumentException_whenPhoneNumberIsInvalid() {
        UserDto user = new UserDto();
        user.setPhone("12345");

        SmsServiceException ex = assertThrows(SmsServiceException.class,
                () -> smsService.send(user, "Test message"));

        assertTrue(ex.getMessage().contains("Invalid phone number"));
    }

    // Универсальный метод для установки приватных полей через reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}