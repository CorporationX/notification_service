package faang.school.notificationservice.kafka;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.LikeEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class LikeEventListener {
    @Qualifier("likeMessageBuilder")
    protected final MessageBuilder<LikeEvent> messageBuilder;
    protected final Map<UserDto.PreferredContact, NotificationService> notificationServicesMap;
    protected final UserServiceClient userServiceClient;

    public LikeEventListener(MessageBuilder<LikeEvent> messageBuilder,
                                 List<NotificationService> services,
                                 UserServiceClient userServiceClient) {
        var servicesMap = new HashMap<UserDto.PreferredContact, NotificationService>();
        services.forEach(service -> servicesMap.put(service.getPreferredContact(), service));
        this.notificationServicesMap = servicesMap;
        this.messageBuilder = messageBuilder;
        this.userServiceClient = userServiceClient;
    }

    @KafkaListener(topics = "like_topic",
            properties = {"spring.json.value.default.type=faang.school.notificationservice.model.LikeEvent"}
    )
    public void onMessage(LikeEvent data) {
        log.info("Received message from like topic kafka");
        UserDto user = userServiceClient.getUser(data.getAuthorId());
        String message = messageBuilder.buildMessage(data, user.getLocale());
        notificationServicesMap.get(user.getPreference()).send(user, message);
    }
}
