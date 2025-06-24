package faang.school.notificationservice.config.kafka;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.event.kafka.SubscriptionEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value(value = "${spring.data.kafka.bootstrap-server}")
    private String bootstrapServer;
    @Value(value = "${spring.data.kafka.consumer-group.notification}")
    private String group;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionEvent> kafkaSubscriptionEventListener() {
        return concurrentKafkaListenerJsonFactory(SubscriptionEvent.class);
    }

    private <T extends NotificationEvent> ConcurrentKafkaListenerContainerFactory<String, T> concurrentKafkaListenerJsonFactory(Class<T> tClass) {
        Map<String, Object> jsonFactoryConfig = new HashMap<>();
        jsonFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        jsonFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        jsonFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();

        ConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<>(
                jsonFactoryConfig,
                new StringDeserializer(),
                new JsonDeserializer<>(tClass, false)
        );

        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}