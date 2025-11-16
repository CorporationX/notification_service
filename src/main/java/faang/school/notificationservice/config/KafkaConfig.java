package faang.school.notificationservice.config;


import faang.school.notificationservice.dto.CommentEventDto;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(KafkaProperties props, Class<T> valueType) {
        return new DefaultKafkaConsumerFactory<>(
                props.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(valueType, false)
        );
    }

    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(
            KafkaProperties props, Class<T> valueType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(props, valueType));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean(name = "commentEventListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CommentEventDto>
    commentEventListenerContainerFactory(KafkaProperties props) {
        ConcurrentKafkaListenerContainerFactory<String, CommentEventDto> factory =
                kafkaListenerContainerFactory(props, CommentEventDto.class);

        factory.setConcurrency(3);

        return factory;
    }
}
