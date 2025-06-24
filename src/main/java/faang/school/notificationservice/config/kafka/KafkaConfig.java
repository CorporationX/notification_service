package faang.school.notificationservice.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public ConsumerFactory<String, ProfileViewEventDto> profileViewConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.getConsumerProperties(ProfileViewEventDto.class),
                new StringDeserializer(),
                new JsonDeserializer<>(ProfileViewEventDto.class, objectMapper, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEventDto> profileViewKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProfileViewEventDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(profileViewConsumerFactory());
        return factory;
    }
}
