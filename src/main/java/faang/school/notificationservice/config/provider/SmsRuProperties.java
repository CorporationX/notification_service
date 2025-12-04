package faang.school.notificationservice.config.provider;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "sms.ru.api")
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SmsRuProperties {
    //@NotBlank
    String key;
   // @NotBlank
    String url;
}
