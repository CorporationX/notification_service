package faang.school.notificationservice.dto.vonage;

import faang.school.notificationservice.config.vonage.DeliveryReceiptsProps;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DeliveryReceipts {

    private final DeliveryReceiptsProps.FieldsName fieldsName;
    private final Map<String, String> values;

    public String getMsisdn() {
        return values.get(fieldsName.getMsisdn());
    }

    public String getTo() {
        return values.get(fieldsName.getTo());
    }

    public String getNetworkCode() {
        return values.get(fieldsName.getNetworkCode());
    }

    public String getMessageId() {
        return values.get(fieldsName.getMessageId());
    }

    public Double getPrice() {
        return Double.valueOf(values.get(fieldsName.getPrice()));
    }

    public DlrStatus getStatus() {
        return DlrStatus.valueOf(values.get(fieldsName.getStatus()));
    }

    public String getScts() {
        return values.get(fieldsName.getScts());
    }

    public ErrorCode getErrorCode() {
        int errorCode = Integer.parseInt(values.get(fieldsName.getErrCode()));
        for (ErrorCode errCode : ErrorCode.values()) {
            int value = errCode.getValue();
            if (value == errorCode) {
                return ErrorCode.fromValue(value);
            }
        }
        return ErrorCode.UKNOWN;
    }

    public Long getClientRef() {
        return Long.valueOf(values.get(fieldsName.getClientRef()));
    }

    public String getApiKey() {
        return values.get(fieldsName.getApiKey());
    }

    public String getMessageTimestamp() {
        return values.get(fieldsName.getMessageTimestamp());
    }
}
