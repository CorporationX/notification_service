package faang.school.notificationservice.exception.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import java.util.List;
import java.util.Locale;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RecommendationRequestedEventListener extends
    AbstractEventListener<RecommendationRequestedEvent> implements MessageListener {

  public RecommendationRequestedEventListener(
      ObjectMapper objectMapper,
      UserServiceClient userServiceClient,
      List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders,
      List<NotificationService> notificationServices) {
    super(objectMapper, userServiceClient, messageBuilders, notificationServices);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    handleEvent(message, RecommendationRequestedEvent.class, event -> {
      String text = getMessage(event, Locale.ROOT);
      sendNotification(event.receiverId(), text);
    });
  }
}
