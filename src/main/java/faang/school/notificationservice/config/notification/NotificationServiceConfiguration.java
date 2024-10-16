package faang.school.notificationservice.config.notification;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.service.sms.SmsService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NotificationServiceConfiguration {

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notificationServices(EmailService emailService,
                                                                                   SmsService smsService,
                                                                                   TelegramService telegramService) {
        Map<UserDto.PreferredContact, NotificationService> services = new HashMap<>();
        services.put(UserDto.PreferredContact.EMAIL, emailService);
        services.put(UserDto.PreferredContact.SMS, smsService);
        services.put(UserDto.PreferredContact.TELEGRAM, telegramService);

        return services;
    }
}

