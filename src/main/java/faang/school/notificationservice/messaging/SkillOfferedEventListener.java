package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import faang.school.notificationservice.notification.SkillOfferedNotificationSender;
import faang.school.notificationservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
@Slf4j
public class SkillOfferedEventListener implements MessageListener {
    private final JsonMapper jsonMapper;
    private final UserServiceClient userClient;
    private final SkillOfferedNotificationSender sender;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferEvent event = getEvent(message);
        log.info("Received event skill offer: {}", event);

        UserDto receiver = userClient.getUserDtoForNotification(event.getReceiverId());
        sender.send(event, receiver);
    }

    private SkillOfferEvent getEvent(Message message) {
        return jsonMapper
                .toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class)
                .orElseThrow(() -> new IllegalArgumentException("Could not deserialize message"));
    }
}
