package faang.school.notificationservice.config;

import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestKafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value(value = "${spring.kafka.topics.goal-completed-topic.name}")
    private String goalCompletedTopic;

    @Value(value = "${spring.kafka.topics.subscription.new-follower-topic.name}")
    private String newFollowerEventTopic;

    @Value(value = "${spring.kafka.topics.subscription.unfollow-topic.name}")
    private String unfollowEventTopic;

    @Value(value = "${spring.kafka.topics.comment-created-topic}")
    private String commentCreatedTopic;

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

    @Bean
    public KafkaTemplate<String, CommentCreationNotificationEvent> kafkaCommentCreationTestTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic goalCompletionTopic() {
        return new NewTopic(goalCompletedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic newFollowerEventsTopic() {
        return new NewTopic(newFollowerEventTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic unfollowEventsTopic() {
        return new NewTopic(unfollowEventTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic commentCreationEventsTopic() {
        return new NewTopic(commentCreatedTopic, 1, (short) 1);
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
