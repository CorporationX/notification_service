package faang.school.notificationservice.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "notification.email")
public class EmailProperties {

    @NotBlank(message = "Email 'from' address is required")
    @Email(message = "Invalid email format for 'from' address")
    private String from;

    private String fromName = "Notification Service";

    private int maxRetries = 3;

    private long retryDelayMs = 1000;

    private boolean enableRetries = true;
}