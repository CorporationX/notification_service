package faang.school.notificationservice.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.kafka.topic")
public record KafkaTopicsProperties(
        @NotBlank String comment
) {
}