package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.dto.VonageDeliveryReceiptsDto;
import faang.school.notificationservice.service.notification.impl.vonage.DeliveryReceiptService;
import faang.school.notificationservice.service.notification.impl.vonage.VonageSmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks/vonage")
@RequiredArgsConstructor
public class VonageWebhookV1Controller {

    private final DeliveryReceiptService deliveryReceiptService;
    private final VonageSmsService vonageSmsService;

    @RequestMapping(value = "/delivery-receipt", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleDeliveryReceipt(@RequestParam VonageDeliveryReceiptsDto deliveryReceiptDto
           /* @RequestParam Map<String, String> deliveryReceipt*/) {
        log.info("handleDeliveryReceipt: {}", deliveryReceiptDto);
        //String status = deliveryReceipt.get("status");
        //String to = deliveryReceipt.get("to");
        //deliveryReceiptService.processDeliveryReceipt(deliveryReceiptDto);
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@RequestBody UserForNotificationDto userForNotificationDto) {
        vonageSmsService.send(userForNotificationDto, "Test message Wyverns s7");
    }
}
