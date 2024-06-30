package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.notification.SmsProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
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
    @InjectMocks
    private SmsService smsService;
    @Mock
    private VonageClient vonageClient;
    @Mock
    private SmsClient smsClient;
    @Mock
    private SmsSubmissionResponse response;
    @Mock
    private SmsProperties smsProperties;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("email@gmail.com")
                .phone("88005553535")
                .build();
    }

    @Nested
    class PositiveTests {
        @Test
        void sendTest() {
            when(vonageClient.getSmsClient()).thenReturn(smsClient);
            when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
            SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
            when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
            when(response.getMessages()).thenReturn(List.of(responseMessage));

            assertDoesNotThrow(() -> smsService.send(userDto, "message"));
        }

        @Test
        void getPreferredContactTest() {
            assertEquals(UserDto.PreferredContact.SMS, smsService.getPreferredContact());
        }
    }

    @Nested
    class NegativeTests {
        @Test
        void sendTest() {
            when(vonageClient.getSmsClient()).thenReturn(smsClient);
            when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
            when(response.getMessages()).thenReturn(null);

            assertDoesNotThrow(() -> smsService.send(userDto, "message"));
        }
    }
}