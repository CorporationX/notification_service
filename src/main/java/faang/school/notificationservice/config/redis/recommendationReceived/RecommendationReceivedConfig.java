package faang.school.notificationservice.config.redis.recommendationReceived;

import faang.school.notificationservice.listener.recommendationReceived.RecommendationReceivedListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RecommendationReceivedConfig {
    @Value("${spring.data.redis.channel.recommendation_received}")
    private String recommendationReceivedChannel;

    @Bean
    MessageListenerAdapter recommendationReceivedAdapter(RecommendationReceivedListener recommendationReceivedListener) {
        return new MessageListenerAdapter(recommendationReceivedListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> recommendationReceived(
            @Qualifier("recommendationReceivedAdapter") MessageListenerAdapter recommendationReceivedAdapter){
        return Pair.of(recommendationReceivedAdapter, new ChannelTopic(recommendationReceivedChannel));
    }
}