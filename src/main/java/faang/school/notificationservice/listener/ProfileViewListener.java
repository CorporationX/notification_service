package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileViewListener implements MessageListener {
    private final AbstractEventListener<ProfileViewEvent> abstractEventListener;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        abstractEventListener.buildAndSendMessage(message, ProfileViewEvent.class);
    }
}