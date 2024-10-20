package faang.school.notificationservice.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Topic {
    private String name;
    private int numPartitions;
    private short replicationFactor;
}
