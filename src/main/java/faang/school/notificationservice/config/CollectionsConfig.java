package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.next.MessageBuilder;
import faang.school.notificationservice.sender.NotificationServiceNext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CollectionsConfig {
    private final List<NotificationServiceNext> notificationServiceNexts;
    private final List<MessageBuilder> messageBuilders;

    @Bean
    public Map<UserDto.PreferredContact, NotificationServiceNext> notifications() {
        Map<UserDto.PreferredContact, NotificationServiceNext> notifications = new HashMap<>(notificationServiceNexts.size());
        for (NotificationServiceNext i : notificationServiceNexts) {
            notifications.put(i.getPreferredContact(), i);
        }
        return notifications;
    }

    @Bean
    public Map<Class<?>, MessageBuilder> messageBuilders() {
        Map<Class<?>, MessageBuilder> builders = new HashMap<>(messageBuilders.size());
        for (MessageBuilder i : messageBuilders) {
            builders.put(i.getClass(), i);
        }
        return builders;
    }
}
