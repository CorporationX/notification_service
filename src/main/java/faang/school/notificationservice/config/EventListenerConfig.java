package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class EventListenerConfig {

    @Bean
    public Map<Class<?>, MessageBuilder<?>> messageBuilderMap(List<MessageBuilder<?>> messageBuilders) {
        return messageBuilders.stream().collect(Collectors.toMap(MessageBuilder::getInstance, Function.identity()));
    }

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notificationServiceMap(List<NotificationService> notificationServices) {
        return notificationServices.stream().collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}