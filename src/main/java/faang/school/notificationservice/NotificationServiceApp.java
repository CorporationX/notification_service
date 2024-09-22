package faang.school.notificationservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("faang.school.notificationservice.client")
@EnableRetry
public class NotificationServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
