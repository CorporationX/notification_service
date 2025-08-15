package faang.school.notificationservice.config.message_source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Конфигурация для получения текстов для нотификаций из конфигурационных файлов
 *
 * @author Linempy
 * @since 14.08.2025
 */
@Slf4j
@Configuration
public class MessageSourceConfig {

    @Value("${message-source.encoding}")
    private String encoding;

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(
                "messages/messages"
        );
        messageSource.setDefaultEncoding(encoding);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
}