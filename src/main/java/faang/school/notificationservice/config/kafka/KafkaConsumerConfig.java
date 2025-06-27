package faang.school.notificationservice.config.kafka;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.event.kafka.SubscriptionEvent;
import faang.school.notificationservice.dto.event.GoalCompletionNotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.data.kafka.bootstrap-server}")
    private String bootstrapServer;
    @Value(value = "${spring.data.kafka.consumer-group.notification}")
    private String group;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionEvent> kafkaSubscriptionEventListener() {
        return concurrentKafkaListenerJsonFactory(SubscriptionEvent.class);
    }

    public ConsumerFactory<String, GoalCompletionNotificationEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(GoalCompletionNotificationEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GoalCompletionNotificationEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GoalCompletionNotificationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
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