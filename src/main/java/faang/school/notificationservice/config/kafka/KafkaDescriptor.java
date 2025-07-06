package faang.school.notificationservice.config.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KafkaDescriptor {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value(value = "${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
}
