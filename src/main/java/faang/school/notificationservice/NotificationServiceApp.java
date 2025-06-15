package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.notificationservice.messaging.telegram.BotKd001;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("faang.school.notificationservice.client")
public class NotificationServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);

        // Register our bot
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotKd001());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
