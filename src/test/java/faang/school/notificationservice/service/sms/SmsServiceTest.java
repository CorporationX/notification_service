package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.VonageConfigurationProperties;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "test";
    private static final String EMAIL = "test@gmail.com";
    private static final String PHONE = "88003000600";
    private static final String MESSAGE = "message";

    @InjectMocks
    private SmsService smsService;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private VonageConfigurationProperties vonageConfigurationProperties;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(USER_ID)
                .username(USERNAME)
                .email(EMAIL)
                .phone(PHONE)
                .preference(UserDto.PreferredContact.SMS)
                .build();
    }

    @Nested
    @DisplayName("Sending SMS")
    class TestSmsSend {

        @Test
        @DisplayName("Should not throw an exception when SMS is successfully sent")
        void successfulSmsSubmissionDoesNotThrowException() {
            when(vonageClient.getSmsClient()).thenReturn(smsClient);
            when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

            SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);

            when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
            when(response.getMessages()).thenReturn(List.of(responseMessage));

            assertDoesNotThrow(() -> smsService.send(userDto, MESSAGE));
        }

        @Test
        @DisplayName("Should not throw an exception when SMS response has no messages")
        void smsSubmissionResponseWithoutMessagesDoesNotThrowException() {
            when(vonageClient.getSmsClient()).thenReturn(smsClient);
            when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
            when(response.getMessages()).thenReturn(null);

            assertDoesNotThrow(() -> smsService.send(userDto, MESSAGE));
        }
    }

    @Nested
    @DisplayName("Preferred Contact Method")
    class TestPreferredContact {

        @Test
        @DisplayName("Should return SMS as the preferred contact method")
        void returnsSmsAsPreferredContactMethod() {
            assertEquals(UserDto.PreferredContact.SMS, smsService.getPreferredContact());
        }
    }
}
