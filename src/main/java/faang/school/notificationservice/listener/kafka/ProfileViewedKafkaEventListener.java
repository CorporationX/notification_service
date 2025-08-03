package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaProperties;
import faang.school.notificationservice.dto.ProfileViewedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewedKafkaEventListener extends AbstractMessageProcessor<ProfileViewedEventDto> {

    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private final Locale absolutelyCustomLocale = Locale.ENGLISH;
    private final KafkaProperties properties;

    public ProfileViewedKafkaEventListener(List<NotificationService> notificationServices,
                                           List<MessageBuilder<ProfileViewedEventDto>> messageBuilders,
                                           KafkaProperties properties,
                                           UserServiceClient userServiceClient,
                                           ObjectMapper objectMapper) {
        super(messageBuilders, notificationServices);
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @KafkaListener(topics = "profile.viewed")
    public void listen(String json) {
        if (!properties.isUseKafka()) return;
        try {
            ProfileViewedEventDto event = objectMapper.readValue(json, ProfileViewedEventDto.class);
            String generalizedNotification = getMessage(absolutelyCustomLocale, event);
            UserDto viewedUser = userServiceClient.getUser(event.getViewerId());
            sendNotification(viewedUser, generalizedNotification);
            log.debug("Notification that profile of user with ID {} was viewed by user with id {} sent ",
                    event.getViewedId(), event.getViewerId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
