package faang.school.notificationservice.service.notification.impl.vonage;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisConfig;
import faang.school.notificationservice.config.vonage.DeliveryReceiptsProps;
import faang.school.notificationservice.dto.user.Language;
import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.dto.vonage.DeliveryReceipts;
import faang.school.notificationservice.dto.vonage.ErrorCode;
import faang.school.notificationservice.exceptions.ResourceNotFoundException;
import faang.school.notificationservice.message.producer.MessagePublisher;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryReceiptServiceTest {

    @Mock
    private SmsMessageService smsMessageService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private VonageSmsService vonageSmsService;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private RedisConfig redisConfig;

    @InjectMocks
    private DeliveryReceiptService deliveryReceiptService;

    private DeliveryReceipts deliveryReceipt;
    private SmsMessage smsMessage;
    private String clientRef;
    private String messageId;
    private double price;
    private Long receiverId = 1L;
    private Map<String, String> values;
    private DeliveryReceiptsProps.FieldsName fieldsNames;

    private String clientRefFieldName;
    private String messageIdFieldName;
    private String priceFieldName;
    private String errorCodeFieldName;

    @BeforeEach
    void setUp() {
        clientRef = "123";
        messageId = "msg123";
        price = 0.05;
        receiverId = 1L;

        clientRefFieldName = "client-ref";
        messageIdFieldName = "message-id";
        priceFieldName = "price";
        errorCodeFieldName = "error-code";

        fieldsNames = new DeliveryReceiptsProps.FieldsName();
        fieldsNames.setClientRef(clientRefFieldName);
        fieldsNames.setMessageId(messageIdFieldName);
        fieldsNames.setPrice(priceFieldName);
        fieldsNames.setErrCode(errorCodeFieldName);


        values = new HashMap<>();
        values.put(clientRefFieldName, clientRef);
        values.put(messageIdFieldName, messageId);
        values.put(priceFieldName, String.valueOf(price));

        smsMessage = SmsMessage.builder()
                .uid(Long.valueOf(clientRef))
                .receiverId(receiverId)
                .content("Test message")
                .deliveryStatus(MessageDeliveryStatus.IN_QUEUE)
                .build();

        when(smsMessageService.getSmsMessageByUid(Long.valueOf(clientRef))).thenReturn(smsMessage);
    }

    @Test
    void testProcessDeliveryReceipt_WhenMessageDelivered_ShouldUpdateStatusToDelivered() {
        values.put(errorCodeFieldName, String.valueOf(ErrorCode.DELIVERED.getValue()));
        deliveryReceipt = new DeliveryReceipts(fieldsNames, values);

        deliveryReceiptService.processDeliveryReceipt(deliveryReceipt);

        ArgumentCaptor<SmsMessage> smsMessageCaptor = ArgumentCaptor.forClass(SmsMessage.class);
        verify(smsMessageService).saveSmsMessageAsync(smsMessageCaptor.capture());

        SmsMessage smsMessage = smsMessageCaptor.getValue();
        assertEquals(MessageDeliveryStatus.DELIVERED, smsMessage.getDeliveryStatus());
        assertEquals(price, smsMessage.getCost());

        verifyNoInteractions(userServiceClient, vonageSmsService, messagePublisher);
    }

    @Test
    void processDeliveryReceipt_WhenErrorIsRetryable_ShouldRetryDelivery() {
        values.put(errorCodeFieldName, String.valueOf(ErrorCode.NETWORK_ERROR.getValue()));
        deliveryReceipt = new DeliveryReceipts(fieldsNames, values);

        UserForNotificationDto receiver = UserForNotificationDto.builder()
                .id(receiverId)
                .username("ya_kokin")
                .email("kolyasik@gmail.com")
                .phone("+1234567890")
                .language(Language.EN)
                .preference(PreferredContact.PHONE)
                .build();

        when(userServiceClient.getUserForNotificationById(receiverId)).thenReturn(receiver);

        deliveryReceiptService.processDeliveryReceipt(deliveryReceipt);

        ArgumentCaptor<SmsMessage> smsMessageCaptor = ArgumentCaptor.forClass(SmsMessage.class);
        verify(smsMessageService).saveSmsMessageAsync(smsMessageCaptor.capture());

        SmsMessage smsMessage = smsMessageCaptor.getValue();
        assertEquals(MessageDeliveryStatus.REPROCESSING, smsMessage.getDeliveryStatus());
        assertEquals(price, smsMessage.getCost());

        verify(userServiceClient).getUserForNotificationById(receiverId);
        verify(vonageSmsService).send(receiver, smsMessage.getContent());
        verifyNoInteractions(messagePublisher);
    }

    @Test
    void processDeliveryReceipt_WhenDeliveryFailed_ShouldPublishToFailedTopic() {
        values.put(errorCodeFieldName, String.valueOf(ErrorCode.UKNOWN.getValue()));
        deliveryReceipt = new DeliveryReceipts(fieldsNames, values);

        String failedTopicName = "failed-messages";
        when(redisConfig.getFailedSmsMessageTopicName()).thenReturn(failedTopicName);

        deliveryReceiptService.processDeliveryReceipt(deliveryReceipt);

        verify(smsMessageService).saveSmsMessageAsync(argThat(message ->
                message.getDeliveryStatus() == MessageDeliveryStatus.DELIVERY_FAILED &&
                        message.getCost() == price
        ));
        verify(messagePublisher).publish(failedTopicName, smsMessage);
        verifyNoInteractions(userServiceClient, vonageSmsService);
    }

    @Test
    void processDeliveryReceipt_WhenSmsMessageNotFound_ShouldThrowException() {
        deliveryReceipt = new DeliveryReceipts(fieldsNames, values);
        when(smsMessageService.getSmsMessageByUid(Long.valueOf(clientRef)))
                .thenThrow(new ResourceNotFoundException("Message not found"));

        assertThrows(ResourceNotFoundException.class, () -> deliveryReceiptService.processDeliveryReceipt(deliveryReceipt)
        );
    }
}
