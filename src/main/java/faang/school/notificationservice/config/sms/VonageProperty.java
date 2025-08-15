package faang.school.notificationservice.config.sms;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "sms.vonage.api")
public record VonageProperty(
        @NonNull String key,
        @NonNull String secret,
        @DefaultValue("CorporationX") String sender
) {}
