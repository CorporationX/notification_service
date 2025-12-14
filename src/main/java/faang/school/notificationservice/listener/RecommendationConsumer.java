package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationEventDto;
import faang.school.notificationservice.exception.JsonProcessingException;
import faang.school.notificationservice.service.MessageBuilderUtils;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationConsumer extends AbstractNotification {
    private final MessageBuilderUtils<RecommendationEventDto> messageBuilderUtils;
    private final ObjectMapper objectMapper;


    public RecommendationConsumer(MessageBuilderUtils<RecommendationEventDto> messageBuilderUtils,
                                  UserServiceClient userServiceClient, List<NotificationService> notificationService,
                                  ObjectMapper objectMapper) {
        super(userServiceClient, notificationService);
        this.objectMapper = objectMapper;
        this.messageBuilderUtils = messageBuilderUtils;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.recommendation}",
            containerFactory = "objectContainerFactory",
            groupId = "${spring.kafka.consumer.group-id.recommendation}")
    public void recommendationConsumer(ConsumerRecord<String, Object> consumerRecord) {
        log.info("Received {} - message from the topic", consumerRecord);
        RecommendationEventDto receivedDto;
        try {
            receivedDto = objectMapper.convertValue(consumerRecord.value(), RecommendationEventDto.class);
        } catch (IllegalArgumentException e) {
            throw new JsonProcessingException("Error to processing convert to receivedDto dto");
        }
        log.info("Convert Successful {}", receivedDto);
        Locale locale = receivedDto.locale() == null ? Locale.getDefault() : receivedDto.locale();
        String text = messageBuilderUtils.getMessage(receivedDto, locale);
        sendNotification(receivedDto.receiverId(), text);
    }
}