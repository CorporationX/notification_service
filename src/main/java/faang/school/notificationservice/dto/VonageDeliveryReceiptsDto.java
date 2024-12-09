package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class VonageDeliveryReceiptsDto {

    @Value("${vonage.delivery-receipts.fields-name.msisdn}")
    private String msisdn;

    @Value("${vonage.delivery-receipts.fields-name.to}")
    private String to;

    @Value("${vonage.delivery-receipts.fields-name.network-code}")
    private String networkCode;

    @Value("${vonage.delivery-receipts.fields-name.messageId}")
    private String messageId;

    @Value("${vonage.delivery-receipts.fields-name.price}")
    private String price;

    @Value("${vonage.delivery-receipts.fields-name.status}")
    private String status;

    @Value("${vonage.delivery-receipts.fields-name.scts}")
    private String scts;

    @Value("${vonage.delivery-receipts.fields-name.err-code}")
    private String errCode;

    @Value("${vonage.delivery-receipts.fields-name.client-ref}")
    private String clientRef;

    @Value("${vonage.delivery-receipts.fields-name.api-key}")
    private String apiKey;

    @Value("${vonage.delivery-receipts.fields-name.message-timestamp}")
    private String messageTimestamp;

    private final Map<String, String> values;

    public String getMsisdn() {

    }
}
