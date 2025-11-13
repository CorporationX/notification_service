package faang.school.notificationservice.config.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms.ru.api")
public record SmsRuProperties(String key, String url) {
}
