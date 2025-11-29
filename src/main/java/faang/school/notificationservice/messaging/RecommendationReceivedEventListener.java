package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.Recommendation;
import faang.school.notificationservice.dto.RecommendationEventBuilder;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder> messageBuilders;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent event =
                    objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class);
            UserDto receive = userServiceClient.getUser(event.getReceiveId());
            UserDto author = userServiceClient.getUser(event.getAuthorId());
            Recommendation recommendation = userServiceClient.getRecommendation(event.getId());
            RecommendationEventBuilder recommendationEventBuilder = RecommendationEventBuilder.builder()
                                .author(author)
                                .receiver(receive)
                                .recommendation(recommendation)
                                .build();

            String text = messageBuilders.stream()
                    .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No message builder found for the given event type: " + event.getClass().getName()))
                    .buildMessage(recommendationEventBuilder, Locale.UK);

            services.stream()
                    .filter(services -> services.getPreferredContact() == UserDto.PreferredContact.EMAIL)
                    .findFirst()
                    .ifPresent(service -> service.send(receive, text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
