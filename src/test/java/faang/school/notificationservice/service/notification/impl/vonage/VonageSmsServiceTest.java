package faang.school.notificationservice.service.notification.impl.vonage;

import cn.hutool.core.lang.Snowflake;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageConfig;
import faang.school.notificationservice.dto.user.Language;
import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.model.MessageDeliveryStatus;
import faang.school.notificationservice.model.SmsMessage;
import faang.school.notificationservice.service.jpa.SmsMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VonageSmsServiceTest {

    @Mock
    private VonageConfig vonageConfig;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsMessageService smsMessageService;

    @Mock
    private SmsClient smsClient;

    @Mock
    private Snowflake snowflake;

    @InjectMocks
    private VonageSmsService vonageSmsService;

    private UserForNotificationDto receiver;
    private String message;
    private Long mockUid;
    private String brand;
    private String callbackUri;

    @BeforeEach
    void setUp() {
        mockUid = 123L;

        receiver = UserForNotificationDto.builder()
                .id(1L)
                .username("ya_kokin")
                .email("kolyasik@gmail.com")
                .phone("+1234567890")
                .language(Language.EN)
                .preference(PreferredContact.PHONE)
                .build();

        message = "Test message";
        brand = "BRAND";
        callbackUri = "http://callback.url";
    }

    @Test
    void testSend_ShouldSendSmsAndSaveMessage() {
        SmsSubmissionResponse mockResponse = mock(SmsSubmissionResponse.class);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(mockResponse);
        when(snowflake.nextId()).thenReturn(mockUid);
        when(vonageConfig.getBrandNumber()).thenReturn(brand);
        when(vonageConfig.getCallbackUrl()).thenReturn(callbackUri);

        vonageSmsService.send(receiver, message);

        ArgumentCaptor<SmsMessage> smsMessageCaptor = ArgumentCaptor.forClass(SmsMessage.class);
        verify(smsMessageService).saveSmsMessageAsync(smsMessageCaptor.capture());

        SmsMessage smsMessage = smsMessageCaptor.getValue();
        assertEquals(mockUid, smsMessage.getUid());
        assertEquals(message, smsMessage.getContent());
        assertEquals(1L, smsMessage.getReceiverId());
        assertEquals(MessageDeliveryStatus.IN_QUEUE, smsMessage.getDeliveryStatus());
        assertNotNull(smsMessage.getSendTime());

        ArgumentCaptor<TextMessage> textMessageCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(smsClient).submitMessage(textMessageCaptor.capture());

        TextMessage textMessage = textMessageCaptor.getValue();
        assertEquals(brand, textMessage.getFrom());
        assertEquals(callbackUri, textMessage.getCallbackUrl());
        assertEquals(message, textMessage.getMessageBody());
        assertEquals(mockUid, Long.valueOf(textMessage.getClientReference()));
    }

    @Test
    void testSend_WhenVonageClientThrowsException_ShouldPropagateException() {
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(snowflake.nextId()).thenReturn(mockUid);
        when(smsClient.submitMessage(any(TextMessage.class)))
                .thenThrow(new RuntimeException("Vonage error"));

        assertThrows(RuntimeException.class, () ->
                vonageSmsService.send(receiver, message)
        );

        verify(smsMessageService).saveSmsMessageAsync(any(SmsMessage.class));
    }

    @Test
    void testGetPreferredContact_ShouldReturnPhone() {
        PreferredContact result = vonageSmsService.getPreferredContact();
        assertEquals(PreferredContact.PHONE, result);
    }
}
