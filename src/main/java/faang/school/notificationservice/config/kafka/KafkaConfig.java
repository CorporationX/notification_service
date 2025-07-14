package faang.school.notificationservice.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MentorshipRequestDto;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;

    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> clazz) {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.getConsumerProperties(clazz),
                new StringDeserializer(),
                new JsonDeserializer<>(clazz, objectMapper, false)
        );
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> listenerFactory(Class<T> clazz, ContainerProperties.AckMode ackMode) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(clazz));
        factory.getContainerProperties().setAckMode(ackMode);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileViewEventDto> profileViewKafkaListenerContainerFactory() {
        return listenerFactory(ProfileViewEventDto.class, ContainerProperties.AckMode.MANUAL);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MentorshipRequestDto> mentorshipEventKafkaListenerContainerFactory() {
        return listenerFactory(MentorshipRequestDto.class, ContainerProperties.AckMode.MANUAL);
    }

}
