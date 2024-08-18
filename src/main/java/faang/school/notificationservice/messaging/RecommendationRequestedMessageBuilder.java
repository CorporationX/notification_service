package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationRequestedMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {

  private final MessageSource messageSource;

  @Override
  public Class<RecommendationRequestedEvent> getInstance() {
    return RecommendationRequestedEvent.class;
  }

  @Override
  public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
    return messageSource.getMessage("recommendation.request",
        new Object[]{event.requesterId()}, locale);
  }

}
