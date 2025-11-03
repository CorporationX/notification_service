package faang.school.notificationservice;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.SmsSendException;
import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    private static final String DEFAULT_PHONE = "+78888888888";
    private static final String EMPTY_PHONE_SPACES = "   ";
    private static final String MESSAGE_TEXT = "Hi!";
    private static final String FROM_NAME = "FaangSchool";
    private static final String DEFAULT_COUNTRY = "RU";

    @Mock
    VonageClient vonageClient;

    @Mock
    SmsClient smsClient;

    @InjectMocks
    SmsService smsService;

    @BeforeEach
    void setUp() {
        smsService = new SmsService(vonageClient, buildProps());
    }

    @Test
    void send_givenServiceDisabled_whenSubmitting_thenSkipsSending() {
        VonageProperties disabledProps = buildProps();
        disabledProps.setEnabled(false);
        smsService = new SmsService(vonageClient, disabledProps);

        UserDto user = new UserDto(1L, DEFAULT_PHONE, UserDto.PreferredContact.PHONE);

        assertDoesNotThrow(() -> smsService.send(user, MESSAGE_TEXT), "Expected no exception when service is disabled");
        verifyNoInteractions(vonageClient);
    }

    @Test
    void send_givenUnnormalizedPhone_whenSubmitting_thenNormalizesPhone() throws Exception {
        String unnormalizedPhone = "88888888"; // Missing country code
        UserDto user = new UserDto(1L, unnormalizedPhone, UserDto.PreferredContact.PHONE);

        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        assertDoesNotThrow(() -> smsService.send(user, MESSAGE_TEXT), "Expected no exception for normalized phone");
        verify(smsClient, times(1)).submitMessage(argThat(msg ->
                msg.getTo().equals("+7" + unnormalizedPhone) // Ensure the phone is normalized
        ));
    }

    @Test
    void send_givenUnexpectedClientError_whenSubmitting_thenThrowsSmsSendException() throws Exception {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any())).thenThrow(new RuntimeException("Unexpected error"));

        UserDto user = new UserDto(1L, DEFAULT_PHONE, UserDto.PreferredContact.PHONE);

        SmsSendException ex = assertThrows(SmsSendException.class, () -> smsService.send(user, MESSAGE_TEXT));
        assertTrue(ex.getMessage().contains("SMS send failed"));
        verify(smsClient, times(1)).submitMessage(any());
    }

    private VonageProperties buildProps() {
        VonageProperties props = new VonageProperties();
        VonageProperties.Api api = new VonageProperties.Api();
        api.setKey("k");
        api.setSecret("s");
        props.setApi(api);
        props.setFrom(FROM_NAME);
        props.setEnabled(true);
        props.setDefaultCountry(DEFAULT_COUNTRY);
        return props;
    }

    @Test
    void send_givenValidPhone_whenSubmitting_thenSendsSuccessfully() throws Exception {
        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        UserDto user = new UserDto(1L, DEFAULT_PHONE, UserDto.PreferredContact.PHONE);

        assertDoesNotThrow(() -> smsService.send(user, MESSAGE_TEXT), "Expected no exception for OK status");
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void send_givenProviderRejected_whenSubmitting_thenThrowsSmsSendException() throws Exception {
        SmsSubmissionResponseMessage failMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(failMsg.getStatus()).thenReturn(MessageStatus.THROTTLED);
        when(failMsg.getErrorText()).thenReturn("Rejected");

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(failMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        UserDto user = new UserDto(1L, DEFAULT_PHONE, UserDto.PreferredContact.PHONE);

        SmsSendException ex = assertThrows(SmsSendException.class, () -> smsService.send(user, MESSAGE_TEXT));
        assertTrue(ex.getMessage().contains("Vonage rejected"));
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void send_givenEmptyPhone_whenValidating_thenThrowsAndDoesNotCallClient() {
        UserDto user = new UserDto(1L, EMPTY_PHONE_SPACES, UserDto.PreferredContact.PHONE);
        assertThrows(SmsSendException.class, () -> smsService.send(user, MESSAGE_TEXT));
        verifyNoInteractions(smsClient);
    }
}