package faang.school.notificationservice.config.serializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class GenericJacksonConfig {

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson() {
        return new GenericJackson2JsonRedisSerializer();
    }
}