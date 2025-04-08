package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

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

    private UserDto validUser;
    private final String validMessage = "Test message";

    @BeforeEach
    void setUp() {
        validUser = UserDto.builder()
                .phone("+1234567890")
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }

    @Test
    void sendShouldSuccessWhenValidInput() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);

        assertDoesNotThrow(() -> smsService.send(validUser, validMessage));

        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void sendShouldThrowWhenVonageClientError() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class)))
                .thenThrow(new VonageClientException("API failure"));

        SmsIntegrationException exception = assertThrows(
                SmsIntegrationException.class,
                () -> smsService.send(validUser, validMessage)
        );

        assertThat(exception.getMessage()).contains("API failure");
    }

    @Test
    void sendShouldThrowWhenEmptyResponseMessages() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(Collections.emptyList());

        SmsIntegrationException exception = assertThrows(
                SmsIntegrationException.class,
                () -> smsService.send(validUser, validMessage)
        );

        assertThat(exception.getMessage()).contains("Empty response");
    }

    @Test
    void sendShouldThrowWhenNonOkStatus() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(responseMessage.getErrorText()).thenReturn("Invalid number");

        SmsIntegrationException exception = assertThrows(
                SmsIntegrationException.class,
                () -> smsService.send(validUser, validMessage)
        );

        assertThat(exception.getMessage()).contains("Invalid number");
    }

    @Test
    void getPreferredContactShouldReturnPhone() {
        assertThat(smsService.getPreferredContact())
                .isEqualTo(UserDto.PreferredContact.PHONE);
    }
}
