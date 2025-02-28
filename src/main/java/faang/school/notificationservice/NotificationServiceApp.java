package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.context.TelegramBotProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("faang.school.notificationservice.client")
@ConfigurationPropertiesScan
public class NotificationServiceApp {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("TELEGRAM_TOKEN", Objects.requireNonNull(dotenv.get("TELEGRAM_TOKEN")));
        System.setProperty("CHAT_ID", Objects.requireNonNull(dotenv.get("CHAT_ID")));

        ConfigurableApplicationContext context = new SpringApplicationBuilder(NotificationServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);

        TelegramService telegramService = context.getBean(TelegramService.class);
        TelegramBotProperties telegramBotProperties = context.getBean(TelegramBotProperties.class);
        UserDto user = new UserDto();
        user.setId(telegramBotProperties.getChatId());
        telegramService.send(user, "Test notification from notification_service FAANG_school");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}