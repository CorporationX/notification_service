package faang.school.notificationservice.config.kafka;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.event.kafka.ViewProfileEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    @Value("spring.kafka.topics.recover.name")
    private String topic;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewFollowerEvent> kafkaNewFollowerEventListener(
            DefaultErrorHandler errorHandler) {
        return concurrentKafkaListenerJsonFactory(NewFollowerEvent.class, errorHandler);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UnfollowEvent> kafkaUnfollowEventListener(
            DefaultErrorHandler errorHandler) {
        return concurrentKafkaListenerJsonFactory(UnfollowEvent.class,errorHandler);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GoalCompletionNotificationEvent> kafkaGoalCompletedEventListener(
            DefaultErrorHandler errorHandler
    ) {
        return concurrentKafkaListenerJsonFactory(GoalCompletionNotificationEvent.class, errorHandler);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ViewProfileEvent> kafkaViewProfileEventListener(
            DefaultErrorHandler errorHandler
    ){
        return concurrentKafkaListenerJsonFactory(ViewProfileEvent.class, errorHandler);
    }

    @Bean
    public DeadLetterPublishingRecoverer recoverer(KafkaTemplate<?, ?> template) {
        return new DeadLetterPublishingRecoverer(template,
                (record, ex) -> new TopicPartition(topic, record.partition()));
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        return new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0));
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> concurrentKafkaListenerJsonFactory(
            Class<T> tClass,
            DefaultErrorHandler errorHandler
    ) {
        Map<String, Object> jsonFactoryConfig = new HashMap<>();
        jsonFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        jsonFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        jsonFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        jsonFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.class);
        jsonFactoryConfig.put("spring.deserializer.value.delegate.class",
                org.springframework.kafka.support.serializer.JsonDeserializer.class.getName());
        jsonFactoryConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        jsonFactoryConfig.put(JsonDeserializer.VALUE_DEFAULT_TYPE, tClass.getName());

        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();

        ConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<>(jsonFactoryConfig);

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}