package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.VonageProperty;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmptyApiResponseException;
import faang.school.notificationservice.exception.MessageSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class VonageSmsServiceTest {
    private VonageSmsService vonageSmsService;
    private VonageProperty vonageProperty;
    @Mock
    private VonageClient vonageClient;
    @Mock
    private SmsClient smsClient;
    @Captor
    private ArgumentCaptor<TextMessage> textMessageCaptor;

    private static final String PHONE = "99991234567";
    private static final String MESSAGE = "Hello from titan-stream-11";

    @BeforeEach
    void setUp() {
        vonageProperty = new VonageProperty("dummy-key", "dummy-secret", "CorporationX");
        vonageSmsService = new VonageSmsService(vonageClient, vonageProperty);
    }

    @Test
    @DisplayName("Успешная отправка SMS по номеру")
    void positive_shouldSendSms() {
        UserDto user = getPreparedUserDto();
        prepareBehavior(MessageStatus.OK);

        vonageSmsService.send(user, MESSAGE);

        verify(vonageClient.getSmsClient(), times(1)).submitMessage(textMessageCaptor.capture());
        String actualMessage = textMessageCaptor.getValue().getMessageBody();
        assertEquals(MESSAGE, actualMessage);
    }

    @Test
    @DisplayName("Ошибка отправки SMS по номеру - нет response")
    void negative_whenResponseNotExists_throwsError() {
        UserDto user = getPreparedUserDto();
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(null);

        assertThrows(EmptyApiResponseException.class,
                     () ->  vonageSmsService.send(user, MESSAGE));
        verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));

    }

    @Test
    @DisplayName("Ошибка отправки SMS по номеру - нет message в response")
    void negative_whenMessagesNotExists_throwsError() {
        UserDto user = getPreparedUserDto();
        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(response.getMessageCount()).thenReturn(0);

        assertThrows(EmptyApiResponseException.class,
                     () ->  vonageSmsService.send(user, MESSAGE));
        verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("Ошибка отправки SMS по номеру - статус не ОК")
    void negative_whenReturnsStatusNotOk_logsError() {
        UserDto user = getPreparedUserDto();
        prepareBehavior(MessageStatus.INVALID_CREDENTIALS);

        assertThrows(MessageSendException.class,
                     () ->  vonageSmsService.send(user, MESSAGE));
        verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));
    }

    // -------------------------

    private UserDto getPreparedUserDto() {
        return UserDto.builder()
                .phone(PHONE)
                .build();
    }

    private void prepareBehavior(MessageStatus messageStatus) {
        SmsSubmissionResponseMessage responseMessage = mock(SmsSubmissionResponseMessage.class);
        SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
        when(responseMessage.getStatus()).thenReturn(messageStatus);
        when(response.getMessageCount()).thenReturn(1);
        when(response.getMessages()).thenReturn(List.of(responseMessage));
    }
}