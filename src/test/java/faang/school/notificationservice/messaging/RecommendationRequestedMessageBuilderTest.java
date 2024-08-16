package faang.school.notificationservice.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestedMessageBuilderTest {

  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private RecommendationRequestedMessageBuilder recommendationRequestedMessageBuilder;

  private RecommendationRequestedEvent recommendationRequestedEvent;

  @Test
  @DisplayName("Проверка на получение экземпляра")
  void testGetInstance() {
    var clazz = RecommendationRequestedEvent.class;
    var instance = recommendationRequestedMessageBuilder.getInstance();
    assertThat(instance).isEqualTo(clazz);
  }

  @Test
  @DisplayName("Проверка на получение сообщения в зависимости от Locale")
  void testBuildMessage() {
    when(messageSource.getMessage("recommendation.request", new Object[]{1L}, Locale.US)).thenReturn("message!");
    String message = recommendationRequestedMessageBuilder.buildMessage(getRecommendationRequestedEvent(), Locale.US);
    verify(messageSource).getMessage("recommendation.request", new Object[]{1L}, Locale.US);
    assertThat(message).isEqualTo("message!");
  }

  private RecommendationRequestedEvent getRecommendationRequestedEvent() {
    return RecommendationRequestedEvent.builder()
        .message("Message")
        .requesterId(1L)
        .receiverId(2L)
        .build();
  }
}