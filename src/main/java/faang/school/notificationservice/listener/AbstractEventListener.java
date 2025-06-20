package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;
    protected final RedisProperties redisProperties;

    public String getMessage(Class<?> classType, Locale locale, T event) {
        MessageBuilder<T> messageBuilder = messageBuilders.stream()
                .filter(ms ->
                        ms.getInstance().equals(classType)).findFirst().orElseThrow();
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        NotificationService notificationService = notificationServices.stream()
                .filter(ns ->
                        ns.getPreferredContact().equals(userDto.getPreference()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No required prefference found."));
        notificationService.send(userDto, message);
    }

    protected abstract List<String> getTopicNameKeys();

    public Set<ChannelTopic> getChannelTopics() {
        return redisProperties.getChannels().entrySet().stream()
                .filter(entry -> getTopicNameKeys().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(ChannelTopic::new)
                .collect(Collectors.toSet());
    }
}
