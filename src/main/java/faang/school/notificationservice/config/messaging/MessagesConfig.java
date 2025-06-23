package faang.school.notificationservice.config.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@RequiredArgsConstructor
public class MessagesConfig {

    private final MessagesProperties messagesProperties;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:" + messagesProperties.getBaseName());
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
