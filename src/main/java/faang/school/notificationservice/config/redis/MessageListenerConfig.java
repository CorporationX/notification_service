//package faang.school.notificationservice.config.redis;
//
//import faang.school.notificationservice.listener.like.LikeEventListener;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//@Configuration
//public class MessageListenerConfig {
//
//    @Value("${spring.data.redis.channel.like}")
//    private String likeChannel;
//
//    @Bean
//    MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
//        return new MessageListenerAdapter(likeEventListener);
//    }
//
//    @Bean
//    ChannelTopic likeChannel() {
//        return new ChannelTopic(likeChannel);
//    }
//}
