package faang.school.notificationservice.config;

import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestKafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, GoalCompletionNotificationEvent> kafkaTestTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, NewFollowerEvent> kafkaNewFollowTestTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, UnfollowEvent> kafkaUnfollowTestTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    private <T> ProducerFactory<String, T> jsonProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }
}
