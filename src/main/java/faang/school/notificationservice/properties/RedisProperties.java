package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.data.redis")
@Getter
@Setter
@Component
public class RedisProperties {
    private int port;
    private String host;
    private Map<String, String> topics;

    public String getTopic(EventType eventType){
        return topics.get(eventType.name());
    }
}