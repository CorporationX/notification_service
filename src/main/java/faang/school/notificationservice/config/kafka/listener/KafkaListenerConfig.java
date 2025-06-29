package faang.school.notificationservice.config.kafka.listener;

import faang.school.notificationservice.dto.ProfileViewedEventDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    @Bean
    public ConsumerFactory<String, ProfileViewedEventDto> profileViewedConsumerFactory(KafkaProperties properties) {
        Map<String, Object> consumerProps = new HashMap<>(properties.buildConsumerProperties());

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "faang.school.notificationservice.dto.ProfileViewedEventDto");

        return new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(ProfileViewedEventDto.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewedEventDto> profileViewedKafkaListenerContainerFactory(
            ConsumerFactory<String, ProfileViewedEventDto> profileViewedConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ProfileViewedEventDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(profileViewedConsumerFactory);
        return factory;
    }

}
