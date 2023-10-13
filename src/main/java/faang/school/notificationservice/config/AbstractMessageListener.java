package faang.school.notificationservice.config;


import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public abstract class AbstractMessageListener {
    protected <T> MessageListenerAdapter createMessageListenerAdapter(T listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}
