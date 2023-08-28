package faang.school.notificationservice.messaging;

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
    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferEvent event = getEvent(message);
    }

    private SkillOfferEvent getEvent(Message message) {
        return jsonMapper
                .toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class)
                .orElseThrow(() -> new IllegalArgumentException("Could not deserialize message"));
    }
}
