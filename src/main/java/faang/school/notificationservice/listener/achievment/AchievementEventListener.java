package faang.school.notificationservice.listener.achievment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.redisevent.AchievementEvent;
import faang.school.notificationservice.service.NotificationSender;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {
    public AchievementEventListener(ObjectMapper objectMapper, NotificationSender notificationSender,
                                    MessageBuilder<AchievementEvent> messageBuilder) {
        super(objectMapper, notificationSender, messageBuilder);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, AchievementEvent.class, event ->
        {
            String messageText = getMessage(event, Locale.UK);
            notificationSender.sendNotification(event.getUserId(), messageText);
        });
    }
}
