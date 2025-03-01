package faang.school.notificationservice.listener.profileview;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.service.NotificationStrategyService;
import org.springframework.data.redis.connection.Message;

public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {
    public ProfileViewEventListener(ObjectMapper objectMapper, NotificationStrategyService notificationStrategyService) {
        super(objectMapper, notificationStrategyService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent event = getEventFromBytes(message.getBody(), ProfileViewEvent.class);
        notificationStrategyService.getNotificationService()
    }
}
