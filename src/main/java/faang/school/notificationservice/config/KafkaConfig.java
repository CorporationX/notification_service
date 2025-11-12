package faang.school.notificationservice.config;


import faang.school.notificationservice.dto.CommentEventDto;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfig {

    public static <T> ConsumerFactory<String, T> createConsumerFactory(KafkaProperties props, Class<T> valueType) {
        return new DefaultKafkaConsumerFactory<>(
                props.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(valueType, false)
        );
    }

    @Bean(value = "consumerFactory")
    public ConsumerFactory<String, CommentEventDto> commentConsumerFactory(KafkaProperties props) {
        return createConsumerFactory(props, CommentEventDto.class);
    }

    @Bean(value = "concurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CommentEventDto> kafkaListenerContainerFactory(
            @Qualifier("consumerFactory") ConsumerFactory<String, CommentEventDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CommentEventDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
