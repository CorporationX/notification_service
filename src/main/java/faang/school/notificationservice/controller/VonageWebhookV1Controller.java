package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.vonage.DeliveryReceipts;
import faang.school.notificationservice.service.notification.impl.vonage.DeliveryReceiptService;
import faang.school.notificationservice.service.notification.impl.vonage.DeliveryReceiptsFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks/vonage")
@RequiredArgsConstructor
public class VonageWebhookV1Controller {

    private final DeliveryReceiptService deliveryReceiptService;
    private final DeliveryReceiptsFactory deliveryReceiptsFactory;

    @RequestMapping(value = "/delivery-receipt", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleDeliveryReceipt(@RequestParam Map<String, String> deliveryReceiptParams) {
        log.info("handleDeliveryReceipt: {}", deliveryReceiptParams);
        DeliveryReceipts deliveryReceipts = deliveryReceiptsFactory.create(deliveryReceiptParams);
        deliveryReceiptService.processDeliveryReceipt(deliveryReceipts);
    }
}
