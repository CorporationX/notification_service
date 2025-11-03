package faang.school.notificationservice;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.error.SmsGatewayException;
import faang.school.notificationservice.sms.VonageSmsGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VonageSmsGatewayImplTest {

    private static final String DEFAULT_PHONE = "+78888888888";
    private static final String EMPTY_PHONE_SPACES = "   ";
    private static final String MESSAGE_TEXT = "Hi!";
    private static final String FROM_NAME = "FaangSchool";
    private static final String DEFAULT_COUNTRY = "RU";

    @Mock
    VonageClient vonageClient;

    @Mock
    SmsClient smsClient;

    VonageSmsGatewayImpl vonageSmsGatewayImpl;

    @BeforeEach
    void setUp() {
        vonageSmsGatewayImpl = new VonageSmsGatewayImpl(vonageClient, buildProps());
    }

    @Test
    void send_givenServiceDisabled_whenSubmitting_thenSkipsSending() {
        VonageProperties disabledProps = buildProps();
        disabledProps.setEnabled(false);
        vonageSmsGatewayImpl = new VonageSmsGatewayImpl(vonageClient, disabledProps);

        assertDoesNotThrow(() -> vonageSmsGatewayImpl.send(DEFAULT_PHONE, MESSAGE_TEXT),
                "Expected no exception when service is disabled");
        verifyNoInteractions(vonageClient);
    }

    @Test
    void send_givenUnnormalizedPhone_whenSubmitting_thenNormalizesPhone() {
        String unnormalizedPhone = "88888888"; // Missing country code

        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        assertDoesNotThrow(() -> vonageSmsGatewayImpl.send(unnormalizedPhone, MESSAGE_TEXT),
                "Expected no exception for normalized phone");
        
        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1)).submitMessage(messageCaptor.capture());
        
        TextMessage capturedMessage = messageCaptor.getValue();
        assertEquals("+7" + unnormalizedPhone, capturedMessage.getTo());
    }

    @Test
    void send_givenValidPhone_whenSubmitting_thenSendsSuccessfully() {
        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);
        when(okMsg.getId()).thenReturn("msg-123");

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        assertDoesNotThrow(() -> vonageSmsGatewayImpl.send(DEFAULT_PHONE, MESSAGE_TEXT),
                "Expected no exception for OK status");
        
        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1)).submitMessage(messageCaptor.capture());
        
        TextMessage capturedMessage = messageCaptor.getValue();
        assertEquals(FROM_NAME, capturedMessage.getFrom());
        assertEquals(DEFAULT_PHONE, capturedMessage.getTo());
        assertEquals(MESSAGE_TEXT, capturedMessage.getMessageBody());
    }

    @Test
    void send_givenProviderRejectedWithThrottled_whenSubmitting_thenThrowsSmsGatewayException() {
        SmsSubmissionResponseMessage failMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(failMsg.getStatus()).thenReturn(MessageStatus.THROTTLED);
        when(failMsg.getErrorText()).thenReturn("Rate limit exceeded");

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(failMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        SmsGatewayException ex = assertThrows(SmsGatewayException.class,
                () -> vonageSmsGatewayImpl.send(DEFAULT_PHONE, MESSAGE_TEXT));
        assertTrue(ex.getMessage().contains("Vonage rejected SMS"));
        assertTrue(ex.getMessage().contains("Rate limit exceeded"));
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void send_givenProviderRejectedWithInvalidParams_whenSubmitting_thenThrowsSmsGatewayException() {
        SmsSubmissionResponseMessage failMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(failMsg.getStatus()).thenReturn(MessageStatus.INVALID_PARAMS);
        when(failMsg.getErrorText()).thenReturn("Invalid recipient");

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(failMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        SmsGatewayException ex = assertThrows(SmsGatewayException.class,
                () -> vonageSmsGatewayImpl.send(DEFAULT_PHONE, MESSAGE_TEXT));
        assertTrue(ex.getMessage().contains("Vonage rejected"));
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void send_givenEmptyPhone_whenValidating_thenThrowsSmsGatewayException() {
        assertThrows(SmsGatewayException.class,
                () -> vonageSmsGatewayImpl.send(EMPTY_PHONE_SPACES, MESSAGE_TEXT),
                "Expected exception for empty phone");
        verifyNoInteractions(smsClient);
    }

    @Test
    void send_givenNullPhone_whenValidating_thenThrowsSmsGatewayException() {
        assertThrows(SmsGatewayException.class,
                () -> vonageSmsGatewayImpl.send(null, MESSAGE_TEXT),
                "Expected exception for null phone");
        verifyNoInteractions(smsClient);
    }

    @Test
    void send_givenUnexpectedClientError_whenSubmitting_thenThrowsSmsGatewayException() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any())).thenThrow(new RuntimeException("Network timeout"));

        SmsGatewayException ex = assertThrows(SmsGatewayException.class,
                () -> vonageSmsGatewayImpl.send(DEFAULT_PHONE, MESSAGE_TEXT));
        assertTrue(ex.getMessage().contains("SMS send failed"));
        assertTrue(ex.getCause().getMessage().contains("Network timeout"));
        verify(smsClient, times(1)).submitMessage(any());
    }

    @Test
    void send_givenPhoneWithPlus_whenNormalizing_thenKeepsOriginalFormat() {
        String internationalPhone = "+1234567890";

        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        vonageSmsGatewayImpl.send(internationalPhone, MESSAGE_TEXT);

        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1)).submitMessage(messageCaptor.capture());
        
        TextMessage capturedMessage = messageCaptor.getValue();
        assertEquals(internationalPhone, capturedMessage.getTo());
    }

    @Test
    void send_givenDifferentCountryCode_whenNormalizing_thenAppliesCorrectPrefix() {
        VonageProperties sgProps = buildProps();
        sgProps.setDefaultCountry("SG");
        sgProps.setCountryCodes(Map.of(
                "SG", "+65",
                "US", "+1",
                "RU", "+7"
        ));
        vonageSmsGatewayImpl = new VonageSmsGatewayImpl(vonageClient, sgProps);

        String localPhone = "91234567";

        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        vonageSmsGatewayImpl.send(localPhone, MESSAGE_TEXT);

        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1)).submitMessage(messageCaptor.capture());
        
        TextMessage capturedMessage = messageCaptor.getValue();
        assertEquals("+6591234567", capturedMessage.getTo());
    }

    @Test
    void send_givenUnknownCountryCode_whenNormalizing_thenUsesDefaultFallback() {
        VonageProperties propsWithUnknown = buildProps();
        propsWithUnknown.setDefaultCountry("XX"); // Unknown country
        vonageSmsGatewayImpl = new VonageSmsGatewayImpl(vonageClient, propsWithUnknown);

        String localPhone = "88888888";

        SmsSubmissionResponseMessage okMsg = mock(SmsSubmissionResponseMessage.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(okMsg.getStatus()).thenReturn(MessageStatus.OK);

        SmsSubmissionResponse resp = mock(SmsSubmissionResponse.class);
        when(resp.getMessages()).thenReturn(List.of(okMsg));
        when(smsClient.submitMessage(any())).thenReturn(resp);

        vonageSmsGatewayImpl.send(localPhone, MESSAGE_TEXT);

        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient, times(1)).submitMessage(messageCaptor.capture());
        
        TextMessage capturedMessage = messageCaptor.getValue();
        assertEquals("+788888888", capturedMessage.getTo());
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
        props.setCountryCodes(Map.of(
                "SG", "+65",
                "US", "+1",
                "GB", "+44",
                "IN", "+91",
                "CN", "+86",
                "RU", "+7"
        ));
        return props;
    }
}