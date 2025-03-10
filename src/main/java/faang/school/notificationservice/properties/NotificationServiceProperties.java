package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "notification-service")
public class NotificationServiceProperties {
  private String apiVersion;
  private TelegramBot telegramBot;

  @Getter
  @Setter
  public static class TelegramBot {
    private String token;
    private String username;
  }
}