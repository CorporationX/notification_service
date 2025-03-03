package faang.school.notificationservice;

import org.springframework.boot.Banner;
import faang.school.notificationservice.config.email.EmailProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("faang.school.notificationservice.client")
@EnableConfigurationProperties(EmailProperties.class)
public class NotificationServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
