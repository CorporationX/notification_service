package faang.school.notificationservice;

import faang.school.notificationservice.properties.TelegramProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("faang.school.notificationservice.client")
@EnableConfigurationProperties(TelegramProperties.class)
public class NotificationServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
