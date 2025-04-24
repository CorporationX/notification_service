package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
    private String writerAchieved;
    private String trustedPackages;
    private Topics topics;

    @Getter
    @Setter
    private static class Topics {
        private String writerAchieved;
    }
}
