package faang.school.notificationservice.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaJsonContainerConfig {

    @Bean
    public RecordMessageConverter kafkaRecordMessageConverter(ObjectMapper objectMapper) {
        return new StringJsonMessageConverter(objectMapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> jsonKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            RecordMessageConverter converter
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(converter);
        return factory;
    }
}
