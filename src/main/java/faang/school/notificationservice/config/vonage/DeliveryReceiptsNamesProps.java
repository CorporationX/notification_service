package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vonage.delivery-receipts.fields-name")
@Data
public class DeliveryReceiptsNamesProps {
    private String msisdn;
    private String to;
    private String networkCode;
    private String messageId;
    private String price;
    private String status;
    private String scts;
    private String errCode;
    private String clientRef;
    private String apiKey;
    private String messageTimestamp;
}
