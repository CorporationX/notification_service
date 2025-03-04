package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class AbstractEventListenerConfig {

    @Bean
    public Map<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap(
            Set<NotificationService> notificationServices) {
        HashMap<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap = new HashMap<>();
        for (NotificationService notificationService : notificationServices) {
            notificationServicesMap.put(notificationService.getPreferredContact(), notificationService);
        }
        return notificationServicesMap;
    }

    @Bean
    public Map<Class<?>, MessageBuilder<?>> messageBuildersMap(
            Set<MessageBuilder<?>> messageBuilders) {
        Map<Class<?>, MessageBuilder<?>> messageBuildersMap = new HashMap<>();
        for (MessageBuilder<?> messageBuilder : messageBuilders) {
            messageBuildersMap.put(messageBuilder.getInstance(), messageBuilder);
        }
        return messageBuildersMap;
    }
}
