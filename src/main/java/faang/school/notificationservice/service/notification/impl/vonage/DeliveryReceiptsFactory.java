package faang.school.notificationservice.service.notification.impl.vonage;

import faang.school.notificationservice.config.vonage.DeliveryReceiptsProps;
import faang.school.notificationservice.dto.vonage.DeliveryReceipts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeliveryReceiptsFactory {

    private final DeliveryReceiptsProps props;

    public DeliveryReceipts create(Map<String, String> values) {
        return new DeliveryReceipts(props.getFieldsName(), values);
    }
}
