package faang.school.notificationservice.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaProperties {

    @Value("${spring.data.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.data.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.data.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.data.kafka.producer.acks}")
    private String acks;
}
