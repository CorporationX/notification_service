package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.PhoneNotificationService;
import faang.school.notificationservice.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class NotificationServiceConfig {

    private final EmailService emailService;
    private final PhoneNotificationService phoneService;
    private final TelegramNotificationService telegramService;

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notificationServiceMap() {
        return Map.of(
                UserDto.PreferredContact.EMAIL, emailService,
                UserDto.PreferredContact.PHONE, phoneService,
                UserDto.PreferredContact.TELEGRAM, telegramService
        );
    }
}