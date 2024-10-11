package faang.school.notificationservice.config.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Имя файла без расширения
        messageSource.setDefaultEncoding("UTF-8"); // Кодировка
        return messageSource;
    }
}
