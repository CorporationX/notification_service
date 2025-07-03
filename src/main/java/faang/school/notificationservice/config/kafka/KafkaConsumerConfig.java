package faang.school.notificationservice.config.kafka;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.event.kafka.EventStartNotificationEvent;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
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
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewFollowerEvent> kafkaNewFollowerEventListener() {
        return concurrentKafkaListenerJsonFactory(NewFollowerEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UnfollowEvent> kafkaUnfollowEventListener() {
        return concurrentKafkaListenerJsonFactory(UnfollowEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GoalCompletionNotificationEvent> kafkaGoalCompletedEventListener() {
        return concurrentKafkaListenerJsonFactory(GoalCompletionNotificationEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventStartNotificationEvent> kafkaEventStartEventListener() {
        return concurrentKafkaListenerJsonFactory(EventStartNotificationEvent.class);
    }

    private <T extends NotificationEvent> ConcurrentKafkaListenerContainerFactory<String, T> concurrentKafkaListenerJsonFactory(Class<T> tClass) {
        Map<String, Object> jsonFactoryConfig = new HashMap<>();
        jsonFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        jsonFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        jsonFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

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