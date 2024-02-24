package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileViewListener implements MessageListener {
    private final AbstractEventListener<ProfileViewEvent> eventListener;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        eventListener.buildAndSendMessage(message, ProfileViewEvent.class);
    }
}