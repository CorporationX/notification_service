package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.dto.vonage.DeliveryReceipts;
import faang.school.notificationservice.service.notification.impl.vonage.DeliveryReceiptService;
import faang.school.notificationservice.service.notification.impl.vonage.DeliveryReceiptsFactory;
import faang.school.notificationservice.service.notification.impl.vonage.VonageSmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks/vonage")
@RequiredArgsConstructor
public class VonageWebhookV1Controller {

    private final DeliveryReceiptService deliveryReceiptService;
    private final VonageSmsService vonageSmsService;
    private final DeliveryReceiptsFactory deliveryReceiptsFactory;
    private final MessageSource messageSource;

    @RequestMapping(value = "/delivery-receipt", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleDeliveryReceipt(@RequestParam Map<String, String> deliveryReceiptParams) {
        log.info("handleDeliveryReceipt: {}", deliveryReceiptParams);
        DeliveryReceipts deliveryReceipts = deliveryReceiptsFactory.create(deliveryReceiptParams);
        deliveryReceiptService.processDeliveryReceipt(deliveryReceipts);
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@RequestBody UserForNotificationDto userForNotificationDto) {
        vonageSmsService.send(userForNotificationDto, "Test message Wyverns s7");
    }
}
