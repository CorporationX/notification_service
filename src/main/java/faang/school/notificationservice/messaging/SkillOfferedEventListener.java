package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.skill.SkillDto;
import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import faang.school.notificationservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class SkillOfferedEventListener implements MessageListener {
    private final JsonMapper jsonMapper;
    private final UserServiceClient client;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferEvent event = getEvent(message);
        sendNotification(event);
    }

    private void sendNotification(SkillOfferEvent event) {
        UserDto sender = client.getUser(event.getSenderId());
        UserDto receiver = client.getUser(event.getReceiverId());
        SkillDto skill = client.getSkillById(event.getSkillId());
    }

    private SkillOfferEvent getEvent(Message message) {
        return jsonMapper
                .toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class)
                .orElseThrow(() -> new IllegalArgumentException("Could not deserialize message"));
    }
}
