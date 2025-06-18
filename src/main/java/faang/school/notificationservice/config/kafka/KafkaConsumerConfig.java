package faang.school.notificationservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
        DefaultKafkaConsumerProperties.class,
        KafkaRetryConfigProperties.class
})
public class KafkaConsumerConfig {
    private final KafkaRetryConfigProperties retryConfigProperties;
    private final DefaultKafkaConsumerProperties kafkaConsumerProperties;
    private final KafkaOperations<String, String> kafkaOperations;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        return new DefaultKafkaConsumerFactory<>(
                defaultConsumerFactory(properties, kafkaConsumerProperties.groupId())
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> recommendationRequestConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(recommendationRequestConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setGroupId(kafkaConsumerProperties.groupId());
        factory.setCommonErrorHandler(defaultErrorHandler());
        return factory;
    }

    private Map<String, Object> defaultConsumerFactory(Map<String, Object> base, String groupId) {
        Map<String, Object> properties = new HashMap<>(base);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.bootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.offsetResetConfig());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerProperties.maxPollRecords());
        return properties;
    }

    private DefaultErrorHandler defaultErrorHandler() {
        BackOff backOff = new FixedBackOff(
                retryConfigProperties.backoffDelay(),
                retryConfigProperties.attempts() - 1
        );
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaOperations,
                (consumerRecord,
                 exception) ->
                        new TopicPartition(consumerRecord.topic() + ".DLT", -1)
        );
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
