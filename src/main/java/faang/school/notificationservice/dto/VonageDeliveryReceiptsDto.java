package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VonageDeliveryReceiptsDto {

    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("to")
    private String to;

    @JsonProperty("network-code")
    private String networkCode;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("status")
    private String status;

    @JsonProperty("scts")
    private String scts;

    @JsonProperty("err-code")
    private Integer errCode;

    @JsonProperty("client-ref")
    private String clientRef;

    @JsonProperty("api-key")
    private String apiKey;

    @JsonProperty("message-timestamp")
    private String messageTimestamp;
}
