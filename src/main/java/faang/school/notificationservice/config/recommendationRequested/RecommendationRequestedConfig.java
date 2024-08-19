package faang.school.notificationservice.config.recommendationRequested;

import faang.school.notificationservice.exception.listener.RecommendationRequestedEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RecommendationRequestedConfig {

  @Value("${spring.data.redis.channel.recommendation_requested.name}")
  private String recommendationRequestedChannel;

  @Bean
  MessageListenerAdapter recommendationRequestedListener(
      RecommendationRequestedEventListener recommendationRequestedEventListener) {
    return new MessageListenerAdapter(recommendationRequestedEventListener);
  }

  @Bean
  Pair<MessageListenerAdapter, ChannelTopic> recommendationRequested(
      @Qualifier("recommendationRequestedListener") MessageListenerAdapter messageListenerAdapter) {
    return Pair.of(messageListenerAdapter, new ChannelTopic(recommendationRequestedChannel));
  }

}
