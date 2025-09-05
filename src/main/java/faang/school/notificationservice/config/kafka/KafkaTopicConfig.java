package faang.school.notificationservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaProperty property;

    @Bean
    public NewTopic commentNewTopic() {
        return TopicBuilder.name(property.topic().commentNew())
                .build();
    }

    @Bean
    public NewTopic skillOfferTopic() {
        return TopicBuilder.name(property.topic().skillOffer())
                .build();
    }
}
