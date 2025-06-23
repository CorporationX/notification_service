package faang.school.notificationservice.config.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "event.messages")
public class EventMessagesProperties {
    private String newLabel;
}
